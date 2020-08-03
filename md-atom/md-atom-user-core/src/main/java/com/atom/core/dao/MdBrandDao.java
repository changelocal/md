package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.SortType;
import com.arc.db.jsd.Sorters;
import com.arc.db.jsd.UpdateValues;
import com.arc.util.data.PageResult;
import com.atom.core.model.MdBrand;
import com.atom.core.model.MdBrandParam;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.*;

/**
 * Created by md on 2020/07/25.
 */
@Repository
public class MdBrandDao extends BaseDao  {

	public PageResult<MdBrand> query(MdBrandParam mdBrandParam) {
		PageResult<MdBrand> result = new PageResult<>();
		Filter filter= Filter.create();

		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandName())){
			filter=filter.and(f("brand_name",mdBrandParam.getBrandName()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandId())){
			filter=filter.and(f("brand_id",mdBrandParam.getBrandId()));
		}
		if(mdBrandParam.getCategory()>0){
			filter=filter.and(f("category",mdBrandParam.getCategory()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getGroup())){
			filter=filter.and(f("group",mdBrandParam.getGroup()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFitProjects())){
			filter=filter.and(f("fit_projects",mdBrandParam.getFitProjects()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFitCategory())){
			filter=filter.and(f("fit_category",mdBrandParam.getFitCategory()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFitEshop())){
			filter=filter.and(f("fit_eshop",mdBrandParam.getFitEshop()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFirstCheckId())){
			filter=filter.and(f("first_check_id",mdBrandParam.getFirstCheckId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getRegisterCheckId())){
			filter=filter.and(f("register_check_id",mdBrandParam.getRegisterCheckId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandType())){
			filter=filter.and(f("brand_type",mdBrandParam.getBrandType()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getImageUrl())){
			filter=filter.and(f("image_url",mdBrandParam.getImageUrl()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getThemeColor())){
			filter=filter.and(f("theme_color",mdBrandParam.getThemeColor()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getVideoUrl())){
			filter=filter.and(f("video_url",mdBrandParam.getVideoUrl()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getLockedOrderId())){
			filter=filter.and(f("locked_order_id",mdBrandParam.getLockedOrderId()));
		}
		if(mdBrandParam.getBrandNameLength()>0){
			filter=filter.and(f("brand_name_length",mdBrandParam.getBrandNameLength()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getSubTitle())){
			filter=filter.and(f("sub_title",mdBrandParam.getSubTitle()));
		}
		if(mdBrandParam.getTotalBuyCount()>0){
			filter=filter.and(f("total_buy_count",mdBrandParam.getTotalBuyCount()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getServiceTypeId())){
			filter=filter.and(f("service_type_id",mdBrandParam.getServiceTypeId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getServiceTypeName())){
			filter=filter.and(f("service_type_name",mdBrandParam.getServiceTypeName()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getKey())){
			filter=filter.and(f("key",mdBrandParam.getKey()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getRegNo())){
			filter=filter.and(f("reg_no",mdBrandParam.getRegNo()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandNatrue())){
			filter=filter.and(f("brand_natrue",mdBrandParam.getBrandNatrue()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getRegMan())){
			filter=filter.and(f("reg_man",mdBrandParam.getRegMan()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getHolderMan())){
			filter=filter.and(f("holder_man",mdBrandParam.getHolderMan()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getContact())){
			filter=filter.and(f("contact",mdBrandParam.getContact()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getExportUserId())){
			filter=filter.and(f("export_user_id",mdBrandParam.getExportUserId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getAgent())){
			filter=filter.and(f("agent",mdBrandParam.getAgent()));
		}
		if(mdBrandParam.getTotalBuyCountInc()>0){
			filter=filter.and(f("total_buy_count_inc",mdBrandParam.getTotalBuyCountInc()));
		}
		if(mdBrandParam.getSuid()>0){
			filter=filter.and(f("suid",mdBrandParam.getSuid()));
		}
		Sorters sorters = t("md_brand").sorters(SortType.ASC,"id");
		long total=(long)this.DB().select(count()).from("md_brand").where(filter).result().value();
		if(total>0){
			List<MdBrand> list = DB().select(MdBrand.class).where(filter).orderBy(sorters)
				.limit((mdBrandParam.getPageIndex()-1)*mdBrandParam.getPageSize(),mdBrandParam.getPageSize())
				.result().all(MdBrand.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<MdBrand> find(MdBrandParam mdBrandParam) {
		Filter filter= Filter.create();

		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandName())){
			filter=filter.and(f("brand_name",mdBrandParam.getBrandName()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandId())){
			filter=filter.and(f("brand_id",mdBrandParam.getBrandId()));
		}
		if(mdBrandParam.getCategory()>0){
			filter=filter.and(f("category",mdBrandParam.getCategory()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getGroup())){
			filter=filter.and(f("group",mdBrandParam.getGroup()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFitProjects())){
			filter=filter.and(f("fit_projects",mdBrandParam.getFitProjects()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFitCategory())){
			filter=filter.and(f("fit_category",mdBrandParam.getFitCategory()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFitEshop())){
			filter=filter.and(f("fit_eshop",mdBrandParam.getFitEshop()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getFirstCheckId())){
			filter=filter.and(f("first_check_id",mdBrandParam.getFirstCheckId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getRegisterCheckId())){
			filter=filter.and(f("register_check_id",mdBrandParam.getRegisterCheckId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandType())){
			filter=filter.and(f("brand_type",mdBrandParam.getBrandType()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getImageUrl())){
			filter=filter.and(f("image_url",mdBrandParam.getImageUrl()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getThemeColor())){
			filter=filter.and(f("theme_color",mdBrandParam.getThemeColor()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getVideoUrl())){
			filter=filter.and(f("video_url",mdBrandParam.getVideoUrl()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getLockedOrderId())){
			filter=filter.and(f("locked_order_id",mdBrandParam.getLockedOrderId()));
		}
		if(mdBrandParam.getBrandNameLength()>0){
			filter=filter.and(f("brand_name_length",mdBrandParam.getBrandNameLength()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getSubTitle())){
			filter=filter.and(f("sub_title",mdBrandParam.getSubTitle()));
		}
		if(mdBrandParam.getTotalBuyCount()>0){
			filter=filter.and(f("total_buy_count",mdBrandParam.getTotalBuyCount()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getServiceTypeId())){
			filter=filter.and(f("service_type_id",mdBrandParam.getServiceTypeId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getServiceTypeName())){
			filter=filter.and(f("service_type_name",mdBrandParam.getServiceTypeName()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getKey())){
			filter=filter.and(f("key",mdBrandParam.getKey()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getRegNo())){
			filter=filter.and(f("reg_no",mdBrandParam.getRegNo()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getBrandNatrue())){
			filter=filter.and(f("brand_natrue",mdBrandParam.getBrandNatrue()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getRegMan())){
			filter=filter.and(f("reg_man",mdBrandParam.getRegMan()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getHolderMan())){
			filter=filter.and(f("holder_man",mdBrandParam.getHolderMan()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getContact())){
			filter=filter.and(f("contact",mdBrandParam.getContact()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getExportUserId())){
			filter=filter.and(f("export_user_id",mdBrandParam.getExportUserId()));
		}
		if(!Strings.isNullOrEmpty(mdBrandParam.getAgent())){
			filter=filter.and(f("agent",mdBrandParam.getAgent()));
		}
		if(mdBrandParam.getTotalBuyCountInc()>0){
			filter=filter.and(f("total_buy_count_inc",mdBrandParam.getTotalBuyCountInc()));
		}

		List<MdBrand> list = DB().select(MdBrand.class)
			.where(filter).result().all(MdBrand.class);
		return list==null?new ArrayList<>():list;
	}

	public MdBrand get(String id) {
		MdBrand result = DB().select(MdBrand.class)
			.where(f("id",id)).result().one(MdBrand.class);
		return result;
	}

	public int add(MdBrand mdBrand) {
		long id = (long) DB().insert(mdBrand).result(true).getKeys().get(0);
		return (int)id;
	}

	public void update(MdBrand mdBrand) {
		UpdateValues updateValues = new UpdateValues();
		if(!Strings.isNullOrEmpty(mdBrand.getId())){
			updateValues.add("id",mdBrand.getId());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getBrandName())){
			updateValues.add("brand_name",mdBrand.getBrandName());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getBrandId())){
			updateValues.add("brand_id",mdBrand.getBrandId());
		}
		if(mdBrand.getCategory()>0){
			updateValues.add("category",mdBrand.getCategory());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getGroup())){
			updateValues.add("group",mdBrand.getGroup());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getFitProjects())){
			updateValues.add("fit_projects",mdBrand.getFitProjects());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getFitCategory())){
			updateValues.add("fit_category",mdBrand.getFitCategory());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getFitEshop())){
			updateValues.add("fit_eshop",mdBrand.getFitEshop());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getFirstCheckId())){
			updateValues.add("first_check_id",mdBrand.getFirstCheckId());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getRegisterCheckId())){
			updateValues.add("register_check_id",mdBrand.getRegisterCheckId());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getBrandType())){
			updateValues.add("brand_type",mdBrand.getBrandType());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getImageUrl())){
			updateValues.add("image_url",mdBrand.getImageUrl());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getThemeColor())){
			updateValues.add("theme_color",mdBrand.getThemeColor());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getVideoUrl())){
			updateValues.add("video_url",mdBrand.getVideoUrl());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getLockedOrderId())){
			updateValues.add("locked_order_id",mdBrand.getLockedOrderId());
		}
		if(mdBrand.getBrandNameLength()>0){
			updateValues.add("brand_name_length",mdBrand.getBrandNameLength());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getSubTitle())){
			updateValues.add("sub_title",mdBrand.getSubTitle());
		}
		if(mdBrand.getTotalBuyCount()>0){
			updateValues.add("total_buy_count",mdBrand.getTotalBuyCount());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getServiceTypeId())){
			updateValues.add("service_type_id",mdBrand.getServiceTypeId());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getServiceTypeName())){
			updateValues.add("service_type_name",mdBrand.getServiceTypeName());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getKey())){
			updateValues.add("key",mdBrand.getKey());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getRegNo())){
			updateValues.add("reg_no",mdBrand.getRegNo());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getBrandNatrue())){
			updateValues.add("brand_natrue",mdBrand.getBrandNatrue());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getRegMan())){
			updateValues.add("reg_man",mdBrand.getRegMan());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getHolderMan())){
			updateValues.add("holder_man",mdBrand.getHolderMan());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getContact())){
			updateValues.add("contact",mdBrand.getContact());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getExportUserId())){
			updateValues.add("export_user_id",mdBrand.getExportUserId());
		}
		if(!Strings.isNullOrEmpty(mdBrand.getAgent())){
			updateValues.add("agent",mdBrand.getAgent());
		}
		if(mdBrand.getTotalBuyCountInc()>0){
			updateValues.add("total_buy_count_inc",mdBrand.getTotalBuyCountInc());
		}
		if(mdBrand.getSuid()>0){
			updateValues.add("suid",mdBrand.getSuid());
		}
		DB().update("md_brand").set(updateValues).where(f("id",mdBrand.getId())).result();
	}

}