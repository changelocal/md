package com.atom.core.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by xxx on 2020/07/25.
 */
@Data
public class MdBrandParam extends MdBrand{
	/**
	 * 当前页
	 */
	private int pageIndex;
	/**
	 * 每页显示条数
	 */
	private int pageSize;
    /**
     * 商标价格区间下限
     */
    private BigDecimal priceLow;

    /**
     * 商标名称长度
     */
    private int brandNameLengthLow;
    /**
     * 商标名称长度
     */
    private int brandNameLengthHigh;

    private List<String> brandIds;
    private Date createTimeBegin;
    private Date createTimeEnd;
    private List<String> groups;
}