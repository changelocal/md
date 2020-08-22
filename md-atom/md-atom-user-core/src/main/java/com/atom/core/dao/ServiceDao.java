package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.SortType;
import com.arc.db.jsd.Sorters;
import com.arc.db.jsd.UpdateValues;
import com.arc.util.data.PageResult;
import com.atom.core.model.Service;
import com.atom.core.model.ServiceParam;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.*;

/**
 * Created by md on 2020/07/25.
 */
@Repository
public class ServiceDao extends BaseDao  {

	public PageResult<Service> query(ServiceParam serviceParam) {
		PageResult<Service> result = new PageResult<>();
		Filter filter= Filter.create();
		if(!Strings.isNullOrEmpty(serviceParam.getId())){
			filter=filter.and(f("id",serviceParam.getId()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getServiceName())){
			filter=filter.and(f("service_name",serviceParam.getServiceName()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getImageUrl())){
			filter=filter.and(f("image_url",serviceParam.getImageUrl()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getVideoUrl())){
			filter=filter.and(f("video_url",serviceParam.getVideoUrl()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getLockedOrderId())){
			filter=filter.and(f("locked_order_id",serviceParam.getLockedOrderId()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getSubTitle())){
			filter=filter.and(f("sub_title",serviceParam.getSubTitle()));
		}
		if(serviceParam.getTotalBuyCount()>0){
			filter=filter.and(f("total_buy_count",serviceParam.getTotalBuyCount()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getServiceTypeId())){
			filter=filter.and(f("service_type_id",serviceParam.getServiceTypeId()));
		}
		if(serviceParam.getTotalBuyCountInc()>0){
			filter=filter.and(f("total_buy_count_inc",serviceParam.getTotalBuyCountInc()));
		}
        if(serviceParam.getIsVideoDefault()>0){
            filter=filter.and(f("is_video_default",serviceParam.getIsVideoDefault()));
        }
		if(serviceParam.getSuid()>0){
			filter=filter.and(f("suid",serviceParam.getSuid()));
		}
		Sorters sorters = t("service").sorters(SortType.ASC,"id");
		long total=(long)this.DB().select(count()).from("service").where(filter).result().value();
		if(total>0){
			List<Service> list = DB().select(Service.class).where(filter).orderBy(sorters)
				.limit((serviceParam.getPageIndex()-1)*serviceParam.getPageSize(),serviceParam.getPageSize())
				.result().all(Service.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<Service> find(ServiceParam serviceParam) {
		Filter filter= Filter.create();
		if(!Strings.isNullOrEmpty(serviceParam.getId())){
			filter=filter.and(f("id",serviceParam.getId()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getServiceName())){
			filter=filter.and(f("service_name",serviceParam.getServiceName()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getImageUrl())){
			filter=filter.and(f("image_url",serviceParam.getImageUrl()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getVideoUrl())){
			filter=filter.and(f("video_url",serviceParam.getVideoUrl()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getLockedOrderId())){
			filter=filter.and(f("locked_order_id",serviceParam.getLockedOrderId()));
		}
		if(!Strings.isNullOrEmpty(serviceParam.getSubTitle())){
			filter=filter.and(f("sub_title",serviceParam.getSubTitle()));
		}
		if(serviceParam.getTotalBuyCount()>0){
			filter=filter.and(f("total_buy_count",serviceParam.getTotalBuyCount()));
		}
        if(serviceParam.getIsEnable()>0){
            filter=filter.and(f("is_enable",serviceParam.getIsEnable()));
        }
        if(serviceParam.getIsChecked()>0){
            filter=filter.and(f("is_checked",serviceParam.getIsChecked()));
        }
        if(serviceParam.getIsVideoDefault()>0){
            filter=filter.and(f("is_video_default",serviceParam.getIsVideoDefault()));
        }
		if(!Strings.isNullOrEmpty(serviceParam.getServiceTypeId())){
			filter=filter.and(f("service_type_id",serviceParam.getServiceTypeId()));
		}
		if(serviceParam.getTotalBuyCountInc()>0){
			filter=filter.and(f("total_buy_count_inc",serviceParam.getTotalBuyCountInc()));
		}
		if(serviceParam.getSuid()>0){
			filter=filter.and(f("suid",serviceParam.getSuid()));
		}
		List<Service> list = DB().select(Service.class)
			.where(filter).result().all(Service.class);
		return list==null?new ArrayList<>():list;
	}

	public Service get(String id) {
		Service result = DB().select(Service.class)
			.where(f("id",id)).result().one(Service.class);
		return result;
	}

	public int add(Service service) {
		long id = (long) DB().insert(service).result(true).getKeys().get(0);
		return (int)id;
	}

	public void update(Service service) {
		UpdateValues updateValues = new UpdateValues();
		if(!Strings.isNullOrEmpty(service.getId())){
			updateValues.add("id",service.getId());
		}
		if(!Strings.isNullOrEmpty(service.getServiceName())){
			updateValues.add("service_name",service.getServiceName());
		}
		if(!Strings.isNullOrEmpty(service.getImageUrl())){
			updateValues.add("image_url",service.getImageUrl());
		}
		if(!Strings.isNullOrEmpty(service.getVideoUrl())){
			updateValues.add("video_url",service.getVideoUrl());
		}
		if(!Strings.isNullOrEmpty(service.getLockedOrderId())){
			updateValues.add("locked_order_id",service.getLockedOrderId());
		}
		if(!Strings.isNullOrEmpty(service.getSubTitle())){
			updateValues.add("sub_title",service.getSubTitle());
		}
		if(service.getTotalBuyCount()>0){
			updateValues.add("total_buy_count",service.getTotalBuyCount());
		}
		if(!Strings.isNullOrEmpty(service.getServiceTypeId())){
			updateValues.add("service_type_id",service.getServiceTypeId());
		}
		if(service.getTotalBuyCountInc()>0){
			updateValues.add("total_buy_count_inc",service.getTotalBuyCountInc());
		}
		if(service.getSuid()>0){
			updateValues.add("suid",service.getSuid());
		}
		DB().update("service").set(updateValues).where(f("id",service.getId())).result();
	}

}