package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.UpdateValues;
import com.atom.core.model.AdminUser;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.f;
@Repository
public class AdminUserDao extends BaseDao {

    public List<AdminUser> query() {
        return DB().select(AdminUser.class).result().all(AdminUser.class);
    }

    public long add(AdminUser adminUser)
    {
        long id = (long) DB().insert(adminUser).result(true).getKeys().get(0);
        return (int)id;
    }

    public void update(AdminUser adminUser) {
        UpdateValues updateValues = new UpdateValues();
        if(!Strings.isNullOrEmpty(adminUser.getId())){
            updateValues.add("id",adminUser.getId());
        }
        if(!Strings.isNullOrEmpty(adminUser.getMobile())){
            updateValues.add("mobile",adminUser.getMobile());
        }
        if(!Strings.isNullOrEmpty(adminUser.getQqAccount())){
            updateValues.add("qq_account",adminUser.getQqAccount());
        }
        if(!Strings.isNullOrEmpty(adminUser.getNickName())){
            updateValues.add("nick_name",adminUser.getNickName());
        }
        if(adminUser.getIsEnable()>0){
            updateValues.add("is_enable",adminUser.getIsEnable());
        }

        DB().update("admin_user").set(updateValues).where(f("id",adminUser.getId())).result();
    }

    public AdminUser get(long id) {
        AdminUser result = DB().select(AdminUser.class)
                            .where(f("id",id)).result().one(AdminUser.class);
        return result;
    }

    public List<AdminUser> find(AdminUser adminUser) {
        Filter filter= Filter.create();
        if(!Strings.isNullOrEmpty(adminUser.getId())){
            filter=filter.and(f("id",adminUser.getId()));
        }
        if(!Strings.isNullOrEmpty(adminUser.getMobile())){
            filter=filter.and(f("mobile",adminUser.getMobile()));
        }
        if(!Strings.isNullOrEmpty(adminUser.getQqAccount())){
            filter=filter.and(f("qq_account",adminUser.getQqAccount()));
        }
        List<AdminUser> list = DB().select(AdminUser.class)
                                .where(filter).result().all(AdminUser.class);
        return list==null?new ArrayList<>():list;
    }
}
