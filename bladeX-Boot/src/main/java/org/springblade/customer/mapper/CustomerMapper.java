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
package org.springblade.customer.mapper;

import org.apache.ibatis.annotations.Param;
import org.springblade.customer.entity.Customer;
import org.springblade.customer.entity.CustomerImg;
import org.springblade.customer.vo.CustomerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author BladeX
 * @since 2020-09-19
 */
public interface CustomerMapper extends BaseMapper<Customer> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param customer
	 * @return
	 */
	List<CustomerVO> selectCustomerPage(IPage page, CustomerVO customer);

	boolean saveCustomerImg(CustomerImg customerImg);

	List<CustomerImg> getCustomerImgs(@Param("customerId") Long customerId);

	boolean removeCustomerImg(@Param("imgName") String imgName);

	String getCreateUserAccount(@Param("createUser")Long createUser);
}
