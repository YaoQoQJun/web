package org.springblade.customer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户图片实体类
 *
 * @author BladeX
 * @since 2020-09-19
 */
@Data
@TableName("cqct_customer_img")
@ApiModel(value = "CustomerImg对象", description = "用户图片表")
public class CustomerImg {

	//客户id
	private Long customerId;

	//图片主url
	private String imgDomain;

	//图片路径
	private String imgLink;

	//图片路径名
	private String imgName;

	//图片真实文件名
	private String imgOriginalName;


}
