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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.oss.minio.MinioTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.entity.CustomerImg;
import org.springblade.customer.service.ICustomerService;
import org.springblade.customer.vo.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
@RequestMapping("blade-customer/customerCount")
@Api(value = "用户统计", tags = "用户统计接口")
public class CustomerCountController extends BladeController {

	private ICustomerService customerService;


	/**
	 * 概况统计
	 */
	@PostMapping("/allCount")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "概况统计", notes = "")
	@ApiLog("概况统计")
	public R<JSONObject> allCount() {

		JSONObject result=new JSONObject();

		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		int month=currCal.get(Calendar.MONTH);

		Date startDate=new Date();
		Date endDate=new Date();


		//至今所有
		startDate=getBeginDate();

		JSONObject zjCount=customerService.allCount(startDate,endDate);
		zjCount.put("name","至今");
		result.put("zjCount",zjCount);


		//今年所有
		startDate=getCurrYearFirst();

		JSONObject jnCount=customerService.allCount(startDate,endDate);
		jnCount.put("name","今年");
		result.put("jnCount",jnCount);

		//上年所有
		startDate=getYearFirst(currentYear-1);
		endDate=getYearLast(currentYear-1);

		JSONObject snCount=customerService.allCount(startDate,endDate);
		snCount.put("name","去年");
		result.put("snCount",snCount);

		//前年所有-上上年
		startDate=getYearFirst(currentYear-2);
		endDate=getYearLast(currentYear-2);

		JSONObject ssnCount=customerService.allCount(startDate,endDate);
		ssnCount.put("name","前年");
		result.put("ssnCount",ssnCount);

		//最近12个月
		JSONArray monthCounts=new JSONArray();

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		for (int i = 0; i <12 ; i++) {
			startDate=getMonthFirst(month-i);
			endDate=getMonthLast(month-i);
			JSONObject monthCount=customerService.allCount(startDate,endDate);
			monthCount.put("name",sdf.format(startDate));
			monthCounts.add(monthCount);
		}

		result.put("monthCounts",monthCounts);

		return R.data(result);
	}


	/**
	 * 个人统计
	 */
	@PostMapping("/personalCount")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "个人统计", notes = "根据起始时间-结束时间 进行个人数据统计")
	@ApiLog("个人统计")
	public R<List> personalCount(@RequestParam("startDate") String startStr,@RequestParam("endDate")String endStr) {

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);

		Date startDate=new Date();
		Date endDate=new Date();

		if(startStr==null||"".equals(startStr)){
			startDate=getMonthFirst(month);
		}else{
			try {
				startDate=sdf.parse(startStr);
			} catch (ParseException e) {
				R.fail("起始时间输入错误");
			}
		}

		if(endStr==null||"".equals(endStr)){
			endDate=new Date();
		}else{
			try {
				endDate=sdf.parse(endStr);
			} catch (ParseException e) {
				R.fail("结束时间输入错误");
			}
		}

		List<HashMap<String,Object>> list=customerService.personalCount(startDate,endDate);


		return R.data(list);
	}


	/**
	 * 获取开始年
	 * @return
	 */
	public static Date getBeginDate(){
		Calendar currCal=Calendar.getInstance();
		currCal.set(Calendar.YEAR,1900);
		return currCal.getTime();
	}

	/**
	 * 获取当月开始时间
	 * @return
	 */
	public static Date getMonthFirst(Integer month){
		Calendar currCal=Calendar.getInstance();
		currCal.set(Calendar.MONTH,month);
		currCal.set(Calendar.DAY_OF_MONTH,1);
		currCal.set(Calendar.HOUR_OF_DAY,0);
		currCal.set(Calendar.MINUTE,0);
		currCal.set(Calendar.SECOND,0);
		return currCal.getTime();
	}

	/**
	 * 获取当月结束时间
	 * @return
	 */
	public static Date getMonthLast(Integer month){
		Calendar currCal=Calendar.getInstance();
		currCal.set(Calendar.MONTH,month);
		currCal.set(Calendar.DAY_OF_MONTH,currCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		currCal.set(Calendar.HOUR_OF_DAY,23);
		currCal.set(Calendar.MINUTE,59);
		currCal.set(Calendar.SECOND,59);
		return currCal.getTime();
	}

	/**
	 * 获取当年的第一天
	 * @return
	 */
	public static Date getCurrYearFirst(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取当年的最后一天
	 * @return
	 */
	public static Date getCurrYearLast(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}

	public static void main(String[] args) {
		Calendar c=Calendar.getInstance();
		int month=c.get(Calendar.MONTH);

		for (int i = 0; i <12 ; i++) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(sdf.format(getMonthFirst(month-i)));
			System.out.println(sdf.format(getMonthLast(month-i)));
			System.out.println("");
		}


	}

}
