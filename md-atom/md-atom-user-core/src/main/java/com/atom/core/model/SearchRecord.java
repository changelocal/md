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
public class SearchRecord {
	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 搜索关键字
	 */
	private String searchWord;
	private String success;

	/**
	 * 注册号
	 */
	private String registNo;

	/**
	 * 注册类别
	 */
	private String registCate;

	/**
	 * 未注册类别
	 */
	private String unregistCate;

	/**
	 * 状态 1发起搜索 2联系完成
	 */
	private int status;

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
	 * 备注
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