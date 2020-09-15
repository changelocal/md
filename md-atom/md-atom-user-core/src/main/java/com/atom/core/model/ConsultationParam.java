package com.atom.core.model;

import lombok.Data;

import java.util.Date;


/**
 * Created by xxx on 2020/07/25.
 */
@Data
public class ConsultationParam extends Consultation{
	/**
	 * 当前页
	 */
	private int pageIndex;
	/**
	 * 每页显示条数
	 */
	private int pageSize;
    private Date createTimeBegin;
    private Date createTimeEnd;
}