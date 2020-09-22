package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

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
	 * 状态 1发起咨询 2完成
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
	 * 买家手机
	 */
	private String buyerMobile;

	/**
	 * 买家名字
	 */
	private String buyerName;

	/**
	 * 
	 */
	private String note;

	/**
	 * 后台运营人id被咨询者
	 */
	private int opUserId;

	/**
	 * 销售手机
	 */
	private String opUserMobile;

	/**
	 * 销售名字
	 */
	private String opUserName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

}