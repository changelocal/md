package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.UpdateValues;
import com.atom.core.model.BrandClass;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.f;

@Repository
public class BrandClassDao extends BaseDao {

    public List<BrandClass> query() {
        return DB().select(BrandClass.class).result().all(BrandClass.class);
    }

    public long add(BrandClass BrandClass)
    {
        long id = (long) DB().insert(BrandClass).result(true).getKeys().get(0);
        return (int)id;
    }

    public void update(BrandClass BrandClass) {
        UpdateValues updateValues = new UpdateValues();
        if(!Strings.isNullOrEmpty(BrandClass.getIsHot())){
            updateValues.add("is_hot",BrandClass.getIsHot());
        }

        DB().update("brand_class").set(updateValues).where(f("id",BrandClass.getId())).result();
    }

    public BrandClass get(String id) {
        BrandClass result = DB().select(BrandClass.class)
                            .where(f("id",id)).result().one(BrandClass.class);
        return result;
    }

    public List<BrandClass> find(BrandClass BrandClass) {
        Filter filter= Filter.create();
        if(!Strings.isNullOrEmpty(BrandClass.getId())){
            filter=filter.and(f("id",BrandClass.getId()));
        }
        if(!Strings.isNullOrEmpty(BrandClass.getCode())){
            filter=filter.and(f("id",BrandClass.getCode()));
        }
        if(!Strings.isNullOrEmpty(BrandClass.getPcode())){
            filter=filter.and(f("id",BrandClass.getPcode()));
        }
        if(!Strings.isNullOrEmpty(BrandClass.getIsHot())){
            filter=filter.and(f("id",BrandClass.getIsHot()));
        }

        List<BrandClass> list = DB().select(BrandClass.class)
                                .where(filter).result().all(BrandClass.class);
        return list==null?new ArrayList<>():list;
    }
}
