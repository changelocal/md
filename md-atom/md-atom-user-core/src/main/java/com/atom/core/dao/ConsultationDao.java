package com.atom.core.dao;

import com.arc.db.jsd.*;
import com.arc.util.data.PageResult;
import com.atom.core.model.Consultation;
import com.atom.core.model.ConsultationParam;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.*;

/**
 * Created by md on 2020/07/25.
 */
@Repository
@Slf4j
public class ConsultationDao extends BaseDao  {

	public PageResult<Consultation> query(ConsultationParam consultationParam) {
	    log.info("consultationParam:"+consultationParam);
		PageResult<Consultation> result = new PageResult<>();
		Filter filter= Filter.create();

//        if(null!=(consultationParam.getCreateTimeBegin())){
//            filter=filter.and(f("create_time", FilterType.GTE,consultationParam.getCreateTimeBegin()));
//        }
//        if(null!=(consultationParam.getCreateTimeEnd())){
//            filter=filter.and(f("create_time",FilterType.LTE,consultationParam.getCreateTimeEnd()));
//        }

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
		if(!Strings.isNullOrEmpty(consultationParam.getBuyerMobile())){
			filter=filter.and(f("buyer_mobile",consultationParam.getBuyerMobile()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getBuyerName())){
			filter=filter.and(f("buyer_name",consultationParam.getBuyerName()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getNote())){
			filter=filter.and(f("note",consultationParam.getNote()));
		}
		if(0<(consultationParam.getOpUserId())){
			filter=filter.and(f("op_user_id",consultationParam.getOpUserId()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpUserMobile())){
			filter=filter.and(f("op_user_mobile",consultationParam.getOpUserMobile()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpUserName())){
			filter=filter.and(f("op_user_name",consultationParam.getOpUserName()));
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
		if(!Strings.isNullOrEmpty(consultationParam.getBuyerMobile())){
			filter=filter.and(f("buyer_mobile",consultationParam.getBuyerMobile()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getBuyerName())){
			filter=filter.and(f("buyer_name",consultationParam.getBuyerName()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getNote())){
			filter=filter.and(f("note",consultationParam.getNote()));
		}
		if(0<(consultationParam.getOpUserId())){
			filter=filter.and(f("op_user_id",consultationParam.getOpUserId()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpUserMobile())){
			filter=filter.and(f("op_user_mobile",consultationParam.getOpUserMobile()));
		}
		if(!Strings.isNullOrEmpty(consultationParam.getOpUserName())){
			filter=filter.and(f("op_user_name",consultationParam.getOpUserName()));
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

	public long add(Consultation consultation) {
        BigInteger id = (BigInteger) DB().insert(consultation).result(true).getKeys().get(0);
		return id.longValue();
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
		if(!Strings.isNullOrEmpty(consultation.getBuyerMobile())){
			updateValues.add("buyer_mobile",consultation.getBuyerMobile());
		}
		if(!Strings.isNullOrEmpty(consultation.getBuyerName())){
			updateValues.add("buyer_name",consultation.getBuyerName());
		}
		if(!Strings.isNullOrEmpty(consultation.getNote())){
			updateValues.add("note",consultation.getNote());
		}
		if(0<(consultation.getOpUserId())){
			updateValues.add("op_user_id",consultation.getOpUserId());
		}
		if(!Strings.isNullOrEmpty(consultation.getOpUserMobile())){
			updateValues.add("op_user_mobile",consultation.getOpUserMobile());
		}
		if(!Strings.isNullOrEmpty(consultation.getOpUserName())){
			updateValues.add("op_user_name",consultation.getOpUserName());
		}
		DB().update("consultation").set(updateValues).where(f("id",consultation.getId())).result();
	}

}