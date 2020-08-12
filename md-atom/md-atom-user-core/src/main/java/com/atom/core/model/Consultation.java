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
public class Consultation {
	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 咨询订单编号
	 */
	private String orderNo;

	/**
	 * 订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成
	 */
	private int status;

	/**
	 * 预付款价格
	 */
	private int prePay;

	/**
	 * 购买用户id
	 */
	private String openId;

	/**
	 * 后台运营人id被咨询者
	 */
	private String opUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

}