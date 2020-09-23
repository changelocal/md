package com.atom.core.dao;

import com.arc.db.jsd.*;
import com.arc.util.data.PageResult;
import com.atom.core.model.BrandOrder;
import com.atom.core.model.BrandOrderParam;
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
public class BrandOrderDao extends BaseDao {

    public long counter() {
        long total = (long) this.DB().select(count()).from("brand_order").result().value();
        return total;
    }

    public PageResult<BrandOrder> query(BrandOrderParam brandOrderParam) {
        PageResult<BrandOrder> result = new PageResult<>();
        Filter filter = Filter.create();

        if (null != (brandOrderParam.getCreateTimeBegin())) {
            filter = filter.and(f("create_time", FilterType.GTE, brandOrderParam.getCreateTimeBegin()));
        }
        if (null != (brandOrderParam.getCreateTimeEnd())) {
            filter = filter.and(f("create_time", FilterType.LTE, brandOrderParam.getCreateTimeEnd()));
        }

        if (brandOrderParam.getStatus() > 0) {
            filter = filter.and(f("status", brandOrderParam.getStatus()));
        }
        if (brandOrderParam.getOrderType() > 0) {
            filter = filter.and(f("order_type", brandOrderParam.getOrderType()));
        }
        if (!Strings.isNullOrEmpty(brandOrderParam.getOrderNo())) {
            filter = filter.and(f("order_no", brandOrderParam.getOrderNo()));
        }
        if (brandOrderParam.getPrePay() > 0) {
            filter = filter.and(f("pre_pay", brandOrderParam.getPrePay()));
        }
        if (brandOrderParam.getRestPay() > 0) {
            filter = filter.and(f("rest_pay", brandOrderParam.getRestPay()));
        }
        if (brandOrderParam.getTotalPay() > 0) {
            filter = filter.and(f("total_pay", brandOrderParam.getTotalPay()));
        }
        if (brandOrderParam.getUserId() > 0) {
            filter = filter.and(f("user_id", brandOrderParam.getUserId()));
        }
        if (brandOrderParam.getOpUserId() > 0) {
            filter = filter.and(f("op_user_id", brandOrderParam.getOpUserId()));
        }
        Sorters sorters = t("brand_order").sorters(SortType.DESC, "create_time");
        long total = (long) this.DB().select(count()).from("brand_order").where(filter).result().value();
        if (total > 0) {
            List<BrandOrder> list = DB().select(BrandOrder.class).where(filter).orderBy(sorters)
                    .limit((brandOrderParam.getPageIndex() - 1) * brandOrderParam.getPageSize(), brandOrderParam.getPageSize())
                    .result().all(BrandOrder.class);
            result.setItems(list);
        }
        result.setTotalCount((int) total);
        return result;
    }

    public List<BrandOrder> find(BrandOrderParam brandOrderParam) {
        Filter filter = Filter.create();
        if (brandOrderParam.getId() > 0) {
            filter = filter.and(f("id", brandOrderParam.getId()));
        }
        if (!Strings.isNullOrEmpty(brandOrderParam.getOrderNo())) {
            filter = filter.and(f("order_no", brandOrderParam.getOrderNo()));
        }
        if (brandOrderParam.getPrePay() > 0) {
            filter = filter.and(f("pre_pay", brandOrderParam.getPrePay()));
        }
        if (brandOrderParam.getRestPay() > 0) {
            filter = filter.and(f("rest_pay", brandOrderParam.getRestPay()));
        }
        if (brandOrderParam.getTotalPay() > 0) {
            filter = filter.and(f("total_pay", brandOrderParam.getTotalPay()));
        }
        if (brandOrderParam.getUserId() > 0) {
            filter = filter.and(f("user_id", brandOrderParam.getUserId()));
        }
        if (brandOrderParam.getOpUserId() > 0) {
            filter = filter.and(f("op_user_id", brandOrderParam.getOpUserId()));
        }
        if (brandOrderParam.getStatus() > 0) {
            filter = filter.and(f("status", brandOrderParam.getStatus()));
        }
        if (!Strings.isNullOrEmpty(brandOrderParam.getProductNo())) {
            filter = filter.and(f("product_no", brandOrderParam.getProductNo()));
        }
        List<BrandOrder> list = DB().select(BrandOrder.class)
                .where(filter).result().all(BrandOrder.class);
        return list == null ? new ArrayList<>() : list;
    }

    public BrandOrder get(int id) {
        BrandOrder result = DB().select(BrandOrder.class)
                .where(f("id", id)).result().one(BrandOrder.class);
        return result;
    }

    public int add(BrandOrder brandOrder) {
        BigInteger id = (BigInteger) DB().insert(brandOrder).result(true).getKeys().get(0);
        return id.intValue();
    }

    public void update(BrandOrder brandOrder) {
        UpdateValues updateValues = new UpdateValues();
        if (brandOrder.getId() > 0) {
            updateValues.add("id", brandOrder.getId());
        }
        if (!Strings.isNullOrEmpty(brandOrder.getOrderNo())) {
            updateValues.add("order_no", brandOrder.getOrderNo());
        }
        if (0 < (brandOrder.getStatus())) {
            updateValues.add("status", brandOrder.getStatus());
        }
        if (null != (brandOrder.getPreTime())) {
            updateValues.add("pre_time", brandOrder.getPreTime());
        }
        if (null != (brandOrder.getOverTime())) {
            updateValues.add("over_time", brandOrder.getOverTime());
        }
        if (brandOrder.getPrePay() > 0) {
            updateValues.add("pre_pay", brandOrder.getPrePay());
        }
        if (brandOrder.getRestPay() > 0) {
            updateValues.add("rest_pay", brandOrder.getRestPay());
        }
        if (brandOrder.getTotalPay() > 0) {
            updateValues.add("total_pay", brandOrder.getTotalPay());
        }
        if (brandOrder.getUserId() > 0) {
            updateValues.add("user_id", brandOrder.getUserId());
        }
        if (brandOrder.getOpUserId() > 0) {
            updateValues.add("op_user_id", brandOrder.getOpUserId());
        }
        if (brandOrder.getUpdateTime() != null) {
            updateValues.add("update_time", brandOrder.getUpdateTime());
        }
        DB().update("brand_order").set(updateValues).where(f("id", brandOrder.getId())).result();
    }

}