package com.atom.core.model;

import lombok.Data;
import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import java.util.Date;
/**
 * Created by xxx on 2020/07/25.
 */
@Data
@JsdTable(nameStyle = NameStyle.LOWER)
public class AdminUser {
	/**
	 * 
	 */
	private String id;

	/**
	 * 
	 */
	private Date createTime;

	/**
	 * 
	 */
	private String account;

	/**
	 * 
	 */
	private String password;

	/**
	 * 0=god,
	 */
	private int type;



	/**
	 * 角色
	 */
	private String role;

	/**
	 * 
	 */
	private String email;

	/**
	 * 
	 */
	private Date lastLoginTime;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 微信-openid 用于微信二维码登录
	 */
	private String wxId;

	/**
	 * 
	 */
	private String avatar;

	/**
	 * 姓名
	 */
	private String nickname;

	/**
	 * 0=禁用，1=启用
	 */
	private int isEnable;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * QQ号
	 */
	private String qqAccount;

	/**
	 * 微信号
	 */
	private String wxAccount;

	/**
	 * 微信二维码名片
	 */
	private String wxQrcode;
	private String title;

}