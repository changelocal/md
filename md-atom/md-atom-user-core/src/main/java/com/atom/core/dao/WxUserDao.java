package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.SortType;
import com.arc.db.jsd.Sorters;
import com.arc.db.jsd.UpdateValues;
import com.arc.util.data.PageResult;
import com.atom.core.model.WxUser;
import com.atom.core.model.WxUserParam;
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
public class WxUserDao extends BaseDao  {

	public PageResult<WxUser> query(WxUserParam wxUserParam) {
		PageResult<WxUser> result = new PageResult<>();
		Filter filter= Filter.create();
		if(wxUserParam.getId()>0){
			filter=filter.and(f("id",wxUserParam.getId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getAppId())){
			filter=filter.and(f("app_id",wxUserParam.getAppId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getUnionId())){
			filter=filter.and(f("union_id",wxUserParam.getUnionId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getOpenId())){
			filter=filter.and(f("open_id",wxUserParam.getOpenId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getMinId())){
			filter=filter.and(f("min_id",wxUserParam.getMinId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getNickName())){
			filter=filter.and(f("nick_name",wxUserParam.getNickName()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getRealName())){
			filter=filter.and(f("real_name",wxUserParam.getRealName()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getIdCard())){
			filter=filter.and(f("id_card",wxUserParam.getIdCard()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getMobile())){
			filter=filter.and(f("mobile",wxUserParam.getMobile()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getAddress())){
			filter=filter.and(f("address",wxUserParam.getAddress()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getBusinessNo())){
			filter=filter.and(f("business_no",wxUserParam.getBusinessNo()));
		}
		Sorters sorters = t("wx_user").sorters(SortType.ASC,"id");
		long total=(long)this.DB().select(count()).from("wx_user").where(filter).result().value();
		if(total>0){
			List<WxUser> list = DB().select(WxUser.class).where(filter).orderBy(sorters)
				.limit((wxUserParam.getPageIndex()-1)*wxUserParam.getPageSize(),wxUserParam.getPageSize())
				.result().all(WxUser.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<WxUser> find(WxUserParam wxUserParam) {
		Filter filter= Filter.create();
		if(wxUserParam.getId()>0){
			filter=filter.and(f("id",wxUserParam.getId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getAppId())){
			filter=filter.and(f("app_id",wxUserParam.getAppId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getUnionId())){
			filter=filter.and(f("union_id",wxUserParam.getUnionId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getOpenId())){
			filter=filter.and(f("open_id",wxUserParam.getOpenId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getMinId())){
			filter=filter.and(f("min_id",wxUserParam.getMinId()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getNickName())){
			filter=filter.and(f("nick_name",wxUserParam.getNickName()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getRealName())){
			filter=filter.and(f("real_name",wxUserParam.getRealName()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getIdCard())){
			filter=filter.and(f("id_card",wxUserParam.getIdCard()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getMobile())){
			filter=filter.and(f("mobile",wxUserParam.getMobile()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getAddress())){
			filter=filter.and(f("address",wxUserParam.getAddress()));
		}
		if(!Strings.isNullOrEmpty(wxUserParam.getBusinessNo())){
			filter=filter.and(f("business_no",wxUserParam.getBusinessNo()));
		}
		List<WxUser> list = DB().select(WxUser.class)
			.where(filter).result().all(WxUser.class);
		return list==null?new ArrayList<>():list;
	}

	public WxUser get(long id) {
		WxUser result = DB().select(WxUser.class)
			.where(f("id",id)).result().one(WxUser.class);
		return result;
	}

	public long add(WxUser wxUser) {
		BigInteger id = (BigInteger) DB().insert(wxUser).result(true).getKeys().get(0);
		return id.longValue();
	}

	public void update(WxUser wxUser) {
		UpdateValues updateValues = new UpdateValues();
		if(wxUser.getId()>0){
			updateValues.add("id",wxUser.getId());
		}
		if(!Strings.isNullOrEmpty(wxUser.getAppId())){
			updateValues.add("app_id",wxUser.getAppId());
		}
		if(!Strings.isNullOrEmpty(wxUser.getUnionId())){
			updateValues.add("union_id",wxUser.getUnionId());
		}
		if(!Strings.isNullOrEmpty(wxUser.getOpenId())){
			updateValues.add("open_id",wxUser.getOpenId());
		}
		if(!Strings.isNullOrEmpty(wxUser.getMinId())){
			updateValues.add("min_id",wxUser.getMinId());
		}
		if(!Strings.isNullOrEmpty(wxUser.getNickName())){
			updateValues.add("nick_name",wxUser.getNickName());
		}
		if(!Strings.isNullOrEmpty(wxUser.getRealName())){
			updateValues.add("real_name",wxUser.getRealName());
		}
		if(!Strings.isNullOrEmpty(wxUser.getIdCard())){
			updateValues.add("id_card",wxUser.getIdCard());
		}
		if(!Strings.isNullOrEmpty(wxUser.getMobile())){
			updateValues.add("mobile",wxUser.getMobile());
		}
		if(!Strings.isNullOrEmpty(wxUser.getAddress())){
			updateValues.add("address",wxUser.getAddress());
		}
		if(!Strings.isNullOrEmpty(wxUser.getBusinessNo())){
			updateValues.add("business_no",wxUser.getBusinessNo());
		}
		DB().update("wx_user").set(updateValues).where(f("id",wxUser.getId())).result();
	}

}