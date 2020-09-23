package com.atom.core.dao;

import com.arc.db.jsd.*;
import com.arc.util.data.PageResult;
import com.atom.core.model.SearchRecord;
import com.atom.core.model.SearchRecordParam;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.*;

/**
 * Created by md on 2020/07/25.
 */
@Repository
public class SearchRecordDao extends BaseDao  {

    public long counter() {
        long total=(long)this.DB().select(count()).from("search_record").result().value();
        return total;
    }

	public PageResult<SearchRecord> query(SearchRecordParam searchRecordParam) {
		PageResult<SearchRecord> result = new PageResult<>();
		Filter filter= Filter.create();

        if(null!=(searchRecordParam.getCreateTimeBegin())){
            filter=filter.and(f("create_time", FilterType.GTE,searchRecordParam.getCreateTimeBegin()));
        }
        if(null!=(searchRecordParam.getCreateTimeEnd())){
            filter=filter.and(f("create_time",FilterType.LTE,searchRecordParam.getCreateTimeEnd()));
        }

		if(searchRecordParam.getId()>0){
			filter=filter.and(f("id",searchRecordParam.getId()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getSearchWord())){
			filter=filter.and(f("search_word",searchRecordParam.getSearchWord()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getRegistNo())){
			filter=filter.and(f("regist_no",searchRecordParam.getRegistNo()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getRegistCate())){
			filter=filter.and(f("regist_cate",searchRecordParam.getRegistCate()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getUnregistCate())){
			filter=filter.and(f("unregist_cate",searchRecordParam.getUnregistCate()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getOpenId())){
			filter=filter.and(f("open_id",searchRecordParam.getOpenId()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getBuyerMobile())){
			filter=filter.and(f("buyer_mobile",searchRecordParam.getBuyerMobile()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getBuyerName())){
			filter=filter.and(f("buyer_name",searchRecordParam.getBuyerName()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getNote())){
			filter=filter.and(f("note",searchRecordParam.getNote()));
		}
		if(0<(searchRecordParam.getOpUserId())){
			filter=filter.and(f("op_user_id",searchRecordParam.getOpUserId()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getOpUserMobile())){
			filter=filter.and(f("op_user_mobile",searchRecordParam.getOpUserMobile()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getOpUserName())){
			filter=filter.and(f("op_user_name",searchRecordParam.getOpUserName()));
		}
		Sorters sorters = t("search_record").sorters(SortType.ASC,"id");
		long total=(long)this.DB().select(count()).from("search_record").where(filter).result().value();
		if(total>0){
			List<SearchRecord> list = DB().select(SearchRecord.class).where(filter).orderBy(sorters)
				.limit((searchRecordParam.getPageIndex()-1)*searchRecordParam.getPageSize(),searchRecordParam.getPageSize())
				.result().all(SearchRecord.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<SearchRecord> find(SearchRecordParam searchRecordParam) {
		Filter filter= Filter.create();
		if(searchRecordParam.getId()>0){
			filter=filter.and(f("id",searchRecordParam.getId()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getSearchWord())){
			filter=filter.and(f("search_word",searchRecordParam.getSearchWord()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getRegistNo())){
			filter=filter.and(f("regist_no",searchRecordParam.getRegistNo()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getRegistCate())){
			filter=filter.and(f("regist_cate",searchRecordParam.getRegistCate()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getUnregistCate())){
			filter=filter.and(f("unregist_cate",searchRecordParam.getUnregistCate()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getOpenId())){
			filter=filter.and(f("open_id",searchRecordParam.getOpenId()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getBuyerMobile())){
			filter=filter.and(f("buyer_mobile",searchRecordParam.getBuyerMobile()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getBuyerName())){
			filter=filter.and(f("buyer_name",searchRecordParam.getBuyerName()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getNote())){
			filter=filter.and(f("note",searchRecordParam.getNote()));
		}
		if(0<(searchRecordParam.getOpUserId())){
			filter=filter.and(f("op_user_id",searchRecordParam.getOpUserId()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getOpUserMobile())){
			filter=filter.and(f("op_user_mobile",searchRecordParam.getOpUserMobile()));
		}
		if(!Strings.isNullOrEmpty(searchRecordParam.getOpUserName())){
			filter=filter.and(f("op_user_name",searchRecordParam.getOpUserName()));
		}
		List<SearchRecord> list = DB().select(SearchRecord.class)
			.where(filter).result().all(SearchRecord.class);
		return list==null?new ArrayList<>():list;
	}

	public SearchRecord get(int id) {
		SearchRecord result = DB().select(SearchRecord.class)
			.where(f("id",id)).result().one(SearchRecord.class);
		return result;
	}

	public int add(SearchRecord searchRecord) {
        BigInteger id = (BigInteger) DB().insert(searchRecord).result(true).getKeys().get(0);
		return id.intValue();
	}

	public void update(SearchRecord searchRecord) {
		UpdateValues updateValues = new UpdateValues();
		if(searchRecord.getId()>0){
			updateValues.add("id",searchRecord.getId());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getSearchWord())){
			updateValues.add("search_word",searchRecord.getSearchWord());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getRegistNo())){
			updateValues.add("regist_no",searchRecord.getRegistNo());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getRegistCate())){
			updateValues.add("regist_cate",searchRecord.getRegistCate());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getUnregistCate())){
			updateValues.add("unregist_cate",searchRecord.getUnregistCate());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getOpenId())){
			updateValues.add("open_id",searchRecord.getOpenId());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getBuyerMobile())){
			updateValues.add("buyer_mobile",searchRecord.getBuyerMobile());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getBuyerName())){
			updateValues.add("buyer_name",searchRecord.getBuyerName());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getNote())){
			updateValues.add("note",searchRecord.getNote());
		}
		if(0<(searchRecord.getOpUserId())){
			updateValues.add("op_user_id",searchRecord.getOpUserId());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getOpUserMobile())){
			updateValues.add("op_user_mobile",searchRecord.getOpUserMobile());
		}
		if(!Strings.isNullOrEmpty(searchRecord.getOpUserName())){
			updateValues.add("op_user_name",searchRecord.getOpUserName());
		}
		DB().update("search_record").set(updateValues).where(f("id",searchRecord.getId())).result();
	}

}