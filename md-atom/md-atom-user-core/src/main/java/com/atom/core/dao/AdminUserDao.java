package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.UpdateValues;
import com.atom.core.model.AdminUser;
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
        if(adminUser.getId()>0){
            updateValues.add("id",adminUser.getId());
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
        if(adminUser.getId()>0){
            filter=filter.and(f("id",adminUser.getId()));
        }


        List<AdminUser> list = DB().select(AdminUser.class)
                                .where(filter).result().all(AdminUser.class);
        return list==null?new ArrayList<>():list;
    }
}
