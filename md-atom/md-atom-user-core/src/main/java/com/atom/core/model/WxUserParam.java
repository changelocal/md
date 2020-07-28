package com.atom.core.model;

import lombok.Data;


import java.time.LocalDateTime;


import java.util.Date;


/**
 * Created by xxx on 2020/07/25.
 */
@Data
public class WxUserParam extends WxUser{
	/**
	 * 当前页
	 */
	private int pageIndex;
	/**
	 * 每页显示条数
	 */
	private int pageSize;
}