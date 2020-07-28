package com.atom.core.model;

import lombok.Data;
import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;

/**
 * Created by xxx on 2020/07/25.
 */
@Data
@JsdTable(nameStyle = NameStyle.LOWER)
public class Brand {
	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 商标名称
	 */
	private String brandName;

	/**
	 * 商标编号
	 */
	private String brandNo;

	/**
	 * 商标图片
	 */
	private String img;

	/**
	 * 最低价格
	 */
	private int minPrice;

	/**
	 * 最高价格
	 */
	private int maxPrice;

	/**
	 * 商标状态
	 */
	private int status;

	/**
	 * 商标文字长度
	 */
	private int nameLength;

	/**
	 * 注册成功率
	 */
	private int registRate;

	/**
	 * 商标组合类型
	 */
	private int unionType;

	/**
	 * 是否注册
	 */
	private int registStatus;

}