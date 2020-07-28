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
public class OrderRefFile {
	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 订单编号
	 */
	private String orderNo;

	/**
	 * 用户id
	 */
	private String userNo;

	/**
	 * 文件编号oss id
	 */
	private String fileId;

	/**
	 * 文件所属1公司2个人
	 */
	private int fileSource;

	/**
	 * 文件类型
	 */
	private int fileType;

	/**
	 * 0 有效 1 删除
	 */
	private int del;

	/**
	 * 创建时间
	 */
	private Date createTime;

}