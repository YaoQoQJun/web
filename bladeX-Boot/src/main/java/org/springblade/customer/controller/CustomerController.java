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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.oss.minio.MinioTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.customer.entity.CustomerImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.vo.CustomerVO;
import org.springblade.customer.service.ICustomerService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.sql.Wrapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("blade-customer/customer")
@Api(value = "用户表", tags = "用户表接口")
public class CustomerController extends BladeController {

	private ICustomerService customerService;

	private MinioTemplate minioTemplate;

	@Autowired
	private Environment env;


	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入customer")
	@ApiLog("客户详情")
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
	@ApiLog("新增客户")
	public R save(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {

		log.info("文件个数："+files.length);
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
			String income=request.getParameter("income");
			String remark=request.getParameter("remark");

			//判断电话是否合法
			if(!isMobile(phone)){
				return R.fail("手机号码格式错误");
			}

			//查询电话唯一
			QueryWrapper<Customer> customerWrapper=new QueryWrapper<>();
			customerWrapper.eq("phone",phone);
			Customer checkCustomer=customerService.getOne(customerWrapper);
			if(checkCustomer!=null){
				return R.fail("手机号码已经存在");
			}

			//判断性别是否合法
			if(sex==null||"".equals(sex)||(!"男".equals(sex)&&!"女".equals(sex))){
				return R.fail("性别不合法");
			}

			//判断年龄是否合法
			if(age==null||age<1||age>500){
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
	@ApiLog("修改客户")
	public R update(@Valid @RequestBody Customer customer) {

		Customer customer1=customerService.getById(customer.getId());
		if(!customer.getPhone().equals(customer1.getPhone())){
			//查询电话唯一
			QueryWrapper<Customer> customerWrapper=new QueryWrapper<>();
			customerWrapper.eq("phone",customer.getPhone());
			Customer checkCustomer=customerService.getOne(customerWrapper);
			if(checkCustomer!=null){
				return R.fail("手机号码已经存在");
			}

		}

		return R.status(customerService.updateById(customer));
	}

	/**
	 * 修改 删除客户图片
	 */
	@PostMapping("/removeCustomerImg")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除用户图片", notes = "传入imgName")
	@ApiLog("删除客户图片")
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
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "保存用户图片", notes = "传入文件以及客户id")
	@ApiLog("新增客户图片")
	public R saveCustomerImg(@RequestParam("file") MultipartFile file,@RequestParam("customerId") Long customerId) {

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
	 * 导出 用户表
	 */
	@PostMapping("/exportCustomer")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "导出客户", notes = "传入ids")
	@ApiLog("导出客户图片")
	public void exportCustomer(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, HttpServletResponse response) {

		String dir=env.getProperty("oss.dir");
		String bucketNmae=env.getProperty("oss.bucket-name");
		String prefixPath=dir+ File.separator+AuthUtil.getTenantId()+"-"+bucketNmae+File.separator;

		List<Long> listIds=Func.toLongList(ids);

		List<Customer> customers=new ArrayList<>();

		for (int i = 0; i < listIds.size() ; i++) {
			Long id=listIds.get(i);
			Customer customer=customerService.getById(id);
			if(customer==null){
				continue;
			}

			if(!customers.contains(customer)){
				customers.add(customer);
			}

		}

		if(customers.size()==0){
			return ;
		}

		XWPFDocument doc = new XWPFDocument();

		//导出所有用户数据
		for (int i = 0; i < customers.size(); i++) {
			Customer customer=customers.get(i);

//--------------------------------------------------姓名开始
			XWPFParagraph p1 = doc.createParagraph();
			if(i!=0){
				p1.setWordWrap(true);
				p1.setPageBreak(true);
			}
			p1.setAlignment(ParagraphAlignment.LEFT);
			p1.setVerticalAlignment(TextAlignment.AUTO);

			XWPFRun r1 = p1.createRun();
			r1.setText("姓名："+(customer.getName()==null?"--":customer.getName()));
			r1.setFontFamily("微软雅黑");
			r1.setTextPosition(20);
//**************************************************姓名结束

//--------------------------------------------------电话开始
			XWPFParagraph p2 = doc.createParagraph();

			p2.setAlignment(ParagraphAlignment.LEFT);
			p2.setVerticalAlignment(TextAlignment.AUTO);

			XWPFRun r2= p2.createRun();
			r2.setText("电话："+customer.getPhone());
			r2.setFontFamily("微软雅黑");
			r2.setTextPosition(20);
//**************************************************电话开始

//--------------------------------------------------年龄开始
			XWPFParagraph p3= doc.createParagraph();

			p3.setAlignment(ParagraphAlignment.LEFT);
			p3.setVerticalAlignment(TextAlignment.AUTO);

			XWPFRun r3= p3.createRun();
			r3.setText("年龄："+customer.getAge());
			r3.setFontFamily("微软雅黑");
			r3.setTextPosition(20);
//**************************************************年龄结束


//--------------------------------------------------性别开始
			XWPFParagraph p4= doc.createParagraph();

			p4.setAlignment(ParagraphAlignment.LEFT);
			p4.setVerticalAlignment(TextAlignment.AUTO);

			XWPFRun r4= p4.createRun();
			r4.setText("性别："+customer.getSex());
			r4.setFontFamily("微软雅黑");
			r4.setTextPosition(20);
//**************************************************性别结束

//--------------------------------------------------备注开始
			XWPFParagraph p5= doc.createParagraph();

			p5.setAlignment(ParagraphAlignment.LEFT);
			p5.setVerticalAlignment(TextAlignment.AUTO);

			XWPFRun r5= p5.createRun();
			r5.setText("备注："+(customer.getRemark()==null?"--":customer.getRemark()));
			r5.setFontFamily("微软雅黑");
			r5.setTextPosition(20);
//**************************************************备注结束

			XWPFParagraph p6 = doc.createParagraph();
			p6.setAlignment(ParagraphAlignment.LEFT);
			p6.setVerticalAlignment(TextAlignment.AUTO);
			XWPFRun r6= p6.createRun();

			List<CustomerImg> customerImgs=customerService.getCustomerImgs(customer.getId());
			for (int j = 0; j < customerImgs.size(); j++) {
				CustomerImg customerImg=customerImgs.get(j);
				String imgName=customerImg.getImgName();
				String imgDir=prefixPath+imgName;

				FileInputStream fis=null;
				try {
					fis=new FileInputStream(imgDir);
				} catch (FileNotFoundException e) {
					continue;
				}

				if(fis==null){
					continue;
				}

				int format;
				if(imgDir.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
				else if(imgDir.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
				else if(imgDir.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
				else if(imgDir.endsWith(".jpeg") || imgDir.endsWith(".jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
				else if(imgDir.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
				else if(imgDir.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
				else if(imgDir.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
				else if(imgDir.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
				else if(imgDir.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
				else if(imgDir.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
				else if(imgDir.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
				else {
					System.err.println("Unsupported picture: " + imgDir +". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
					continue;
				}

				try {
					r6.addPicture(fis,format, customerImg.getImgOriginalName(), Units.toEMU(500), Units.toEMU(250));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}

		}

		try {

			response.setContentType("application/x-download");
			response.setCharacterEncoding("UTF-8");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(AuthUtil.getUserAccount()+"_"+sdf.format(new Date())+".docx", "UTF-8"));

			File f=new File(prefixPath+"down_word");
			if(!f.exists()){
				f.mkdir();
			}

			doc.write(response.getOutputStream());

			sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d=new Date();
			for (int i = 0; i < customers.size(); i++) {
				Customer customer=customers.get(i);
				customer.setExportTime(d);
				customer.setExportNum(customer.getExportNum()+1);
				customer.setStatus(2);

				if(customer.getExportNum()==1){
					customer.setExportTimeDetail(sdf.format(d));
				}else{
					customer.setExportTimeDetail(customer.getExportTimeDetail()+" ， "+sdf.format(d));
				}

				customerService.updateById(customer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 新增或修改 用户表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入customer")
	@ApiLog("修改客户资料")
	public R submit(@Valid @RequestBody Customer customer) {

		Customer customer1=customerService.getById(customer.getId());
		if(!customer.getPhone().equals(customer1.getPhone())){
			//查询电话唯一
			QueryWrapper<Customer> customerWrapper=new QueryWrapper<>();
			customerWrapper.eq("phone",customer.getPhone());
			Customer checkCustomer=customerService.getOne(customerWrapper);
			if(checkCustomer!=null){
				return R.fail("手机号码已经存在");
			}

		}

		return R.status(customerService.saveOrUpdate(customer));
	}

	
	/**
	 * 删除 用户表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	@ApiLog("删除客户资料")
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
		p = Pattern.compile("^[1][1,2,3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}


}
