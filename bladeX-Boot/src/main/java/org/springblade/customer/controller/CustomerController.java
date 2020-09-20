/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.customer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.oss.minio.MinioTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.customer.entity.CustomerImg;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.vo.CustomerVO;
import org.springblade.customer.service.ICustomerService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户表 控制器
 *
 * @author BladeX
 * @since 2020-09-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-customer/customer")
@Api(value = "用户表", tags = "用户表接口")
public class CustomerController extends BladeController {

	private ICustomerService customerService;

	private MinioTemplate minioTemplate;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入customer")
	public R<CustomerVO> detail(CustomerVO customer) {

		Customer detail = customerService.getOne(Condition.getQueryWrapper(customer));
		JSONObject detailJson=JSONObject.parseObject(JSONObject.toJSONString(detail));
		customer=detailJson.toJavaObject(CustomerVO.class);

		List<CustomerImg> customerImgs=customerService.getCustomerImgs(detail.getId());
		customer.setCustomerImgs(customerImgs);


		customer.setCreateUserAccount(customerService.getCreateUserAccount(detail.getCreateUser()));

		return R.data(customer);
	}

	/**
	 * 分页 用户表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入customer")
	public R<IPage<Customer>> list(Customer customer, Query query) {
		IPage<Customer> pages = customerService.page(Condition.getPage(query), Condition.getQueryWrapper(customer));
		return R.data(pages);
	}

	/**
	 * 自定义分页 用户表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入customer")
	public R<IPage<CustomerVO>> page(CustomerVO customer, Query query) {
		IPage<CustomerVO> pages = customerService.selectCustomerPage(Condition.getPage(query), customer);
		return R.data(pages);
	}

	/**
	 * 新增 用户表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入customer")
	public R save(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {

		//上传图片
		List<BladeFile> fileList=new ArrayList<>();

		for (int i = 0; i < files.length; i++) {
			MultipartFile file=files[i];
			BladeFile bladeFile=minioTemplate.putFile(file);
			if(bladeFile!=null){
				fileList.add(bladeFile);
			}
		}

		//新增用户
		Customer customer=new Customer();

		try{

			String name=request.getParameter("name");
			String email=request.getParameter("email");
			String phone=request.getParameter("phone");
			Integer age=Integer.parseInt(request.getParameter("age"));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

			Date birthday=null;
			if(request.getParameter("birthday")!=null&&!"".equals(request.getParameter("birthday"))){
				birthday=sdf.parse(request.getParameter("birthday"));
			}

			String sex=request.getParameter("sex");
			String address=request.getParameter("address");
			Double income=(request.getParameter("income")==null||"".equals(request.getParameter("income")))?0:Double.parseDouble(request.getParameter("income"));
			String remark=request.getParameter("remark");

			//判断电话是否合法
			if(!isMobile(phone)){
				return R.fail("电话不合法");
			}

			//判断性别是否合法
			if(sex==null||"".equals(sex)||(!"男".equals(sex)&&!"女".equals(sex))){
				return R.fail("性别不合法");
			}

			//判断年龄是否合法
			if(age==null||age<=16||age>=100){
				return R.fail("年龄不合法");
			}

			customer.setName(name);
			customer.setEmail(email);
			customer.setPhone(phone);
			customer.setAge(age);
			customer.setBirthday(birthday);
			customer.setSex(sex);
			customer.setAddress(address);
			customer.setIncome(income);
			customer.setRemark(remark);

		}catch (Exception e){
			e.printStackTrace();
			return R.fail("参数错误："+e.getMessage());
		}

		boolean saveFlag=customerService.save(customer);
		if(saveFlag==false){
			return	R.fail(400,"保存客户错误");
		}

		for (int i = 0; i <fileList.size() ; i++) {

			BladeFile bladeFile=fileList.get(i);
			CustomerImg customerImg=new CustomerImg();
			customerImg.setCustomerId(customer.getId());
			customerImg.setImgDomain(bladeFile.getDomain());
			customerImg.setImgLink(bladeFile.getLink());
			customerImg.setImgName(bladeFile.getName());
			customerImg.setImgOriginalName(bladeFile.getOriginalName());

			saveFlag=customerService.saveCustomerImg(customerImg);

			if(saveFlag==false){
				return	R.fail(400,"客户图片保存错误");
			}

		}

		return R.status(true);

	}

	/**
	 * 修改 用户表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入customer")
	public R update(@Valid @RequestBody Customer customer) {
		return R.status(customerService.updateById(customer));
	}

	/**
	 * 修改 删除客户图片
	 */
	@PostMapping("/removeCustomerImg")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除用户图片", notes = "传入imgName")
	public R removeCustomerImg(@RequestParam("imgName") String imgName) {
		boolean flag=customerService.removeCustomerImg(imgName);
		if(flag){
			minioTemplate.removeFile(imgName);
		}
		return R.status(flag);
	}

	/**
	 * 修改 保存客户图片
	 */
	@PostMapping("/saveCustomerImg")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "保存用户图片", notes = "传入文件以及客户id")
	public R removeCustomerImg(@RequestParam("file") MultipartFile file,@RequestParam("customerId") Long customerId) {

		Customer customer=customerService.getById(customerId);
		if(customer==null){
			return R.fail("客户不存在");
		}

		BladeFile bladeFile=minioTemplate.putFile(file);

		CustomerImg customerImg=new CustomerImg();
		customerImg.setCustomerId(customerId);
		customerImg.setImgDomain(bladeFile.getDomain());
		customerImg.setImgLink(bladeFile.getLink());
		customerImg.setImgName(bladeFile.getName());
		customerImg.setImgOriginalName(bladeFile.getOriginalName());

		boolean saveFlag=customerService.saveCustomerImg(customerImg);

		return R.status(saveFlag);
	}

	/**
	 * 新增或修改 用户表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入customer")
	public R submit(@Valid @RequestBody Customer customer) {
		return R.status(customerService.saveOrUpdate(customer));
	}

	
	/**
	 * 删除 用户表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(customerService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 * 手机号验证
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(final String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][1,2,3,4,5,6,7,,9][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}


}
