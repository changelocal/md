package com.atom.core.dao;

import com.google.common.base.Strings;
import com.atom.core.dao.BaseDao;
import com.atom.core.dao.OrderRefFileDao;
import com.atom.core.model.OrderRefFile;
import com.atom.core.model.OrderRefFileParam;
import org.springframework.stereotype.Repository;
import com.arc.db.jsd.Filter;
import com.arc.db.jsd.SortType;
import com.arc.db.jsd.Sorters;
import com.arc.db.jsd.UpdateValues;
import com.arc.util.data.PageResult;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.count;
import static com.arc.db.jsd.Shortcut.f;
import static com.arc.db.jsd.Shortcut.t;

/**
 * Created by md on 2020/07/25.
 */
@Repository
public class OrderRefFileDao extends BaseDao  {

	public PageResult<OrderRefFile> query(OrderRefFileParam orderRefFileParam) {
		PageResult<OrderRefFile> result = new PageResult<>();
		Filter filter= Filter.create();
		if(orderRefFileParam.getId()>0){
			filter=filter.and(f("id",orderRefFileParam.getId()));
		}
		if(!Strings.isNullOrEmpty(orderRefFileParam.getOrderNo())){
			filter=filter.and(f("order_no",orderRefFileParam.getOrderNo()));
		}
		if(!Strings.isNullOrEmpty(orderRefFileParam.getUserNo())){
			filter=filter.and(f("user_no",orderRefFileParam.getUserNo()));
		}
		if(!Strings.isNullOrEmpty(orderRefFileParam.getFileId())){
			filter=filter.and(f("file_id",orderRefFileParam.getFileId()));
		}
		Sorters sorters = t("order_ref_file").sorters(SortType.ASC,"id");
		long total=(long)this.DB().select(count()).from("order_ref_file").where(filter).result().value();
		if(total>0){
			List<OrderRefFile> list = DB().select(OrderRefFile.class).where(filter).orderBy(sorters)
				.limit((orderRefFileParam.getPageIndex()-1)*orderRefFileParam.getPageSize(),orderRefFileParam.getPageSize())
				.result().all(OrderRefFile.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<OrderRefFile> find(OrderRefFileParam orderRefFileParam) {
		Filter filter= Filter.create();
		if(orderRefFileParam.getId()>0){
			filter=filter.and(f("id",orderRefFileParam.getId()));
		}
		if(!Strings.isNullOrEmpty(orderRefFileParam.getOrderNo())){
			filter=filter.and(f("order_no",orderRefFileParam.getOrderNo()));
		}
		if(!Strings.isNullOrEmpty(orderRefFileParam.getUserNo())){
			filter=filter.and(f("user_no",orderRefFileParam.getUserNo()));
		}
		if(!Strings.isNullOrEmpty(orderRefFileParam.getFileId())){
			filter=filter.and(f("file_id",orderRefFileParam.getFileId()));
		}
		List<OrderRefFile> list = DB().select(OrderRefFile.class)
			.where(filter).result().all(OrderRefFile.class);
		return list==null?new ArrayList<>():list;
	}

	public OrderRefFile get(int id) {
		OrderRefFile result = DB().select(OrderRefFile.class)
			.where(f("id",id)).result().one(OrderRefFile.class);
		return result;
	}

	public int add(OrderRefFile orderRefFile) {
		long id = (long) DB().insert(orderRefFile).result(true).getKeys().get(0);
		return (int)id;
	}

	public void update(OrderRefFile orderRefFile) {
		UpdateValues updateValues = new UpdateValues();
		if(orderRefFile.getId()>0){
			updateValues.add("id",orderRefFile.getId());
		}
		if(!Strings.isNullOrEmpty(orderRefFile.getOrderNo())){
			updateValues.add("order_no",orderRefFile.getOrderNo());
		}
		if(!Strings.isNullOrEmpty(orderRefFile.getUserNo())){
			updateValues.add("user_no",orderRefFile.getUserNo());
		}
		if(!Strings.isNullOrEmpty(orderRefFile.getFileId())){
			updateValues.add("file_id",orderRefFile.getFileId());
		}
		DB().update("order_ref_file").set(updateValues).where(f("id",orderRefFile.getId())).result();
	}

}