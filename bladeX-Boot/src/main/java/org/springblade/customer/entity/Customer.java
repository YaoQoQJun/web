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
package org.springblade.customer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springblade.core.mp.base.BaseEntity;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 用户表实体类
 *
 * @author BladeX
 * @since 2020-09-19
 */
@Data
@TableName("cqct_customer")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Customer对象", description = "用户表")
public class Customer extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 姓名
	*/
		@ApiModelProperty(value = "姓名")
		private String name;
	/**
	* 邮箱
	*/
		@ApiModelProperty(value = "邮箱")
		private String email;
	/**
	* 手机
	*/
		@ApiModelProperty(value = "手机")
		@Size(min = 11,max = 11)
		private String phone;
	/**
	* 年龄
	*/
		@ApiModelProperty(value = "年龄")
		@Min(value = 16)
		@Max(value = 100)
		private Integer age;
	/**
	* 生日
	*/
		@ApiModelProperty(value = "生日")
		@DateTimeFormat(
			pattern = "yyyy-MM-dd"
		)
		@JsonFormat(
			pattern = "yyyy-MM-dd"
		)
		private Date birthday;
	/**
	* 性别
	*/
		@ApiModelProperty(value = "性别")
		@NotEmpty
		private String sex;
	/**
	* 地址
	*/
		@ApiModelProperty(value = "地址")
		private String address;
	/**
	* 收入
	*/
		@ApiModelProperty(value = "收入")
		private Double income;
	/**
	* 导出时间
	*/
		@ApiModelProperty(value = "导出时间")
		@DateTimeFormat(
			pattern = "yyyy-MM-dd"
		)
		@JsonFormat(
			pattern = "yyyy-MM-dd"
		)
		private Date exportTime;
	/**
	* 导出次数
	*/
		@ApiModelProperty(value = "导出次数")
		private Integer exportNum;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;


}
