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
package org.springblade.customer.service;

import com.alibaba.fastjson.JSONObject;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.entity.CustomerImg;
import org.springblade.customer.vo.CustomerVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户表 服务类
 *
 * @author BladeX
 * @since 2020-09-19
 */
public interface ICustomerService extends BaseService<Customer> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param customer
	 * @return
	 */
	IPage<CustomerVO> selectCustomerPage(IPage<CustomerVO> page, CustomerVO customer);


	/**
	 * 保存用户图片
	 * @param customerImg
	 * @return
	 */
	boolean saveCustomerImg(CustomerImg customerImg);

	/**
	 * 根据客户ID查询客户所有图片
	 * @param customerId
	 * @return
	 */
	List<CustomerImg> getCustomerImgs(Long customerId);

	/**
	 * 删除客户图片
	 * @param imgName
	 * @return
	 */
	boolean removeCustomerImg(String imgName);

	/**
	 * 获取创建人
	 * @param createUser
	 * @return
	 */
	String getCreateUserAccount(Long createUser);

	/**
	 * 根据时间区间概况统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	JSONObject allCount(Date startDate, Date endDate);

	/**
	 * 根据时间区间个人统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<HashMap<String, Object>> personalCount(Date startDate, Date endDate);
}
