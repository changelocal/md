package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.SortType;
import com.arc.db.jsd.Sorters;
import com.arc.db.jsd.UpdateValues;
import com.arc.util.data.PageResult;
import com.atom.core.model.Consultation;
import com.atom.core.model.ConsultationParam;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.*;

/**
 * Created by md on 2020/07/25.
 */
@Repository
public class ConsultationDao extends BaseDao  {

	public PageResult<Consultation> query(ConsultationParam consultationParam) {
		PageResult<Consultation> result = new PageResult<>();
		Filter filter= Filter.create();
		if(consultationParam.getId()>0){
			filter=filter.and(f("id",consultationParam.getId()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOrderNo())){
			filter=filter.and(f("order_no",consultationParam.getOrderNo()));
		}
        if((consultationParam.getStatus())>0){
            filter=filter.and(f("status",consultationParam.getStatus()));
        }
		if(consultationParam.getPrePay()>0){
			filter=filter.and(f("pre_pay",consultationParam.getPrePay()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpenId())){
			filter=filter.and(f("open_id",consultationParam.getOpenId()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpUserId())){
			filter=filter.and(f("op_user_id",consultationParam.getOpUserId()));
		}
		Sorters sorters = t("consultation").sorters(SortType.DESC,"create_time");
		long total=(long)this.DB().select(count()).from("consultation").where(filter).result().value();
		if(total>0){
			List<Consultation> list = DB().select(Consultation.class).where(filter).orderBy(sorters)
				.limit((consultationParam.getPageIndex()-1)*consultationParam.getPageSize(),consultationParam.getPageSize())
				.result().all(Consultation.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<Consultation> find(ConsultationParam consultationParam) {
		Filter filter= Filter.create();
		if(consultationParam.getId()>0){
			filter=filter.and(f("id",consultationParam.getId()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOrderNo())){
			filter=filter.and(f("order_no",consultationParam.getOrderNo()));
		}
		if(consultationParam.getPrePay()>0){
			filter=filter.and(f("pre_pay",consultationParam.getPrePay()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpenId())){
			filter=filter.and(f("open_id",consultationParam.getOpenId()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpUserId())){
			filter=filter.and(f("op_user_id",consultationParam.getOpUserId()));
		}
		List<Consultation> list = DB().select(Consultation.class)
			.where(filter).result().all(Consultation.class);
		return list==null?new ArrayList<>():list;
	}

	public Consultation get(int id) {
		Consultation result = DB().select(Consultation.class)
			.where(f("id",id)).result().one(Consultation.class);
		return result;
	}

	public int add(Consultation consultation) {
		long id = (long) DB().insert(consultation).result(true).getKeys().get(0);
		return (int)id;
	}

	public void update(Consultation consultation) {
		UpdateValues updateValues = new UpdateValues();
		if(consultation.getId()>0){
			updateValues.add("id",consultation.getId());
		}
		if(!Strings.isNullOrEmpty(consultation.getOrderNo())){
			updateValues.add("order_no",consultation.getOrderNo());
		}
		if(consultation.getPrePay()>0){
			updateValues.add("pre_pay",consultation.getPrePay());
		}
		if(!Strings.isNullOrEmpty(consultation.getOpenId())){
			updateValues.add("open_id",consultation.getOpenId());
		}
		if(!Strings.isNullOrEmpty(consultation.getOpUserId())){
			updateValues.add("op_user_id",consultation.getOpUserId());
		}
		DB().update("consultation").set(updateValues).where(f("id",consultation.getId())).result();
	}

}