package com.atom.core.model;

import lombok.Data;
import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * Created by xxx on 2020/07/25.
 */
@Data
@JsdTable(nameStyle = NameStyle.LOWER)
public class BrandOrder {
	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 订单编号
	 */
	private String orderNo;

	/**
	 * 订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成
	 */
	private int status;

	/**
	 * 1商标注册订单2商标维权订单3商标信息变更订单
	 */
	private int orderType;

	/**
	 * 预付款价格
	 */
	private int prePay;

	/**
	 * 尾款价格
	 */
	private int restPay;

	/**
	 * 总价
	 */
	private int totalPay;

	/**
	 * 购买用户id
	 */
	private int userId;

	/**
	 * 后台运营下单人id
	 */
	private int opUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 预支付时间
	 */
	private Date preTime;

	/**
	 * 尾款支付时间
	 */
	private Date overTime;

	/**
	 * 产品编号
	 */
	private String productNo;

}