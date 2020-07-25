package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.UpdateValues;
import com.atom.core.model.WxUser;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.f;

@Repository
public class WxUserDao extends BaseDao {

    public List<WxUser> query() {
        return DB().select(WxUser.class).result().all(WxUser.class);
    }

    public long add(WxUser wxUser)
    {
        long id = (long) DB().insert(wxUser).result(true).getKeys().get(0);
        return (int)id;
    }

    public void update(WxUser wxUser) {
        UpdateValues updateValues = new UpdateValues();
        if(!Strings.isNullOrEmpty(wxUser.getId())){
            updateValues.add("id",wxUser.getId());
        }
        if(!Strings.isNullOrEmpty(wxUser.getMobile())){
            updateValues.add("mobile",wxUser.getMobile());
        }

        if(!Strings.isNullOrEmpty(wxUser.getNickName())){
            updateValues.add("nick_name",wxUser.getNickName());
        }
        if(wxUser.getIsEnable()>0){
            updateValues.add("is_enable",wxUser.getIsEnable());
        }

        DB().update("wx_user").set(updateValues).where(f("id",wxUser.getId())).result();
    }

    public WxUser get(long id) {
        WxUser result = DB().select(WxUser.class)
                            .where(f("id",id)).result().one(WxUser.class);
        return result;
    }

    public List<WxUser> find(WxUser wxUser) {
        Filter filter= Filter.create();
        if(!Strings.isNullOrEmpty(wxUser.getId())){
            filter=filter.and(f("id",wxUser.getId()));
        }
        if(!Strings.isNullOrEmpty(wxUser.getMobile())){
            filter=filter.and(f("mobile",wxUser.getMobile()));
        }
        if(!Strings.isNullOrEmpty(wxUser.getOpenId())){
            filter=filter.and(f("open_id",wxUser.getOpenId()));
        }
        if((wxUser.getIsEnable()>0)){
            filter=filter.and(f("is_enable",wxUser.getIsEnable()));
        }
        List<WxUser> list = DB().select(WxUser.class)
                                .where(filter).result().all(WxUser.class);
        return list==null?new ArrayList<>():list;
    }
}
