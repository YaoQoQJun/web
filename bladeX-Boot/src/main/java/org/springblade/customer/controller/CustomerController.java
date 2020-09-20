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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.vo.CustomerVO;
import org.springblade.customer.service.ICustomerService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.Arrays;

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

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入customer")
	public R<Customer> detail(Customer customer) {
		Customer detail = customerService.getOne(Condition.getQueryWrapper(customer));
		return R.data(detail);
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
		System.out.println(customer.getCreateTimeRange());
		IPage<CustomerVO> pages = customerService.selectCustomerPage(Condition.getPage(query), customer);
		return R.data(pages);
	}

	/**
	 * 新增 用户表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入customer")
	public R save(@Valid @RequestBody Customer customer) {
		return R.status(customerService.save(customer));
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

	
}
