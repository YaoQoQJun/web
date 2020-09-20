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
package org.springblade.customer.service.impl;

import org.springblade.core.oss.model.BladeFile;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.entity.CustomerImg;
import org.springblade.customer.vo.CustomerVO;
import org.springblade.customer.mapper.CustomerMapper;
import org.springblade.customer.service.ICustomerService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 用户表 服务实现类
 *
 * @author BladeX
 * @since 2020-09-19
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, Customer> implements ICustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public IPage<CustomerVO> selectCustomerPage(IPage<CustomerVO> page, CustomerVO customer) {
		return page.setRecords(baseMapper.selectCustomerPage(page, customer));
	}

	@Override
	public boolean saveCustomerImg(CustomerImg customerImg) {
		return customerMapper.saveCustomerImg(customerImg);
	}

	@Override
	public List<CustomerImg> getCustomerImgs(Long customerId) {
		return customerMapper.getCustomerImgs(customerId);
	}

	@Override
	public boolean removeCustomerImg(String imgName) {
		return customerMapper.removeCustomerImg(imgName);
	}

	@Override
	public String getCreateUserAccount(Long createUser) {
		return customerMapper.getCreateUserAccount(createUser);
	}

}
