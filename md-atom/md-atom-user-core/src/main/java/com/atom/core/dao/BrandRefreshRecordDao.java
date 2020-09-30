package com.atom.core.dao;

import com.google.common.base.Strings;
import com.atom.core.dao.BaseDao;
import com.atom.core.dao.BrandRefreshRecordDao;
import com.atom.core.model.BrandRefreshRecord;
import com.atom.core.model.BrandRefreshRecordParam;
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
public class BrandRefreshRecordDao extends BaseDao  {

	public PageResult<BrandRefreshRecord> query(BrandRefreshRecordParam brandRefreshRecordParam) {
		PageResult<BrandRefreshRecord> result = new PageResult<>();
		Filter filter= Filter.create();
		if(brandRefreshRecordParam.getId()>0){
			filter=filter.and(f("id",brandRefreshRecordParam.getId()));
		}
		if(!Strings.isNullOrEmpty(brandRefreshRecordParam.getNote())){
			filter=filter.and(f("note",brandRefreshRecordParam.getNote()));
		}
		Sorters sorters = t("brand_refresh_record").sorters(SortType.ASC,"id");
		long total=(long)this.DB().select(count()).from("brand_refresh_record").where(filter).result().value();
		if(total>0){
			List<BrandRefreshRecord> list = DB().select(BrandRefreshRecord.class).where(filter).orderBy(sorters)
				.limit((brandRefreshRecordParam.getPageIndex()-1)*brandRefreshRecordParam.getPageSize(),brandRefreshRecordParam.getPageSize())
				.result().all(BrandRefreshRecord.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<BrandRefreshRecord> find(BrandRefreshRecordParam brandRefreshRecordParam) {
		Filter filter= Filter.create();
		if(brandRefreshRecordParam.getId()>0){
			filter=filter.and(f("id",brandRefreshRecordParam.getId()));
		}
		if(!Strings.isNullOrEmpty(brandRefreshRecordParam.getNote())){
			filter=filter.and(f("note",brandRefreshRecordParam.getNote()));
		}
		List<BrandRefreshRecord> list = DB().select(BrandRefreshRecord.class)
			.where(filter).result().all(BrandRefreshRecord.class);
		return list==null?new ArrayList<>():list;
	}

	public BrandRefreshRecord get(int id) {
		BrandRefreshRecord result = DB().select(BrandRefreshRecord.class)
			.where(f("id",id)).result().one(BrandRefreshRecord.class);
		return result;
	}

	public int add(BrandRefreshRecord brandRefreshRecord) {
		long id = (long) DB().insert(brandRefreshRecord).result(true).getKeys().get(0);
		return (int)id;
	}

	public void update(BrandRefreshRecord brandRefreshRecord) {
		UpdateValues updateValues = new UpdateValues();
		if(brandRefreshRecord.getId()>0){
			updateValues.add("id",brandRefreshRecord.getId());
		}
		if(!Strings.isNullOrEmpty(brandRefreshRecord.getNote())){
			updateValues.add("note",brandRefreshRecord.getNote());
		}
		DB().update("brand_refresh_record").set(updateValues).where(f("id",brandRefreshRecord.getId())).result();
	}

}