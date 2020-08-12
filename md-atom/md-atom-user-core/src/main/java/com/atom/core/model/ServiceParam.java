package com.atom.core.model;

import lombok.Data;


/**
 * Created by xxx on 2020/07/25.
 */
@Data
public class ServiceParam extends Service{
	/**
	 * 当前页
	 */
	private int pageIndex;
	/**
	 * 每页显示条数
	 */
	private int pageSize;
}