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
public class BrandRefreshRecord {
	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

}