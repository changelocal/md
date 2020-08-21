package com.atom.core.dao;

import com.arc.db.jsd.Filter;
import com.arc.db.jsd.SortType;
import com.arc.db.jsd.Sorters;
import com.arc.db.jsd.UpdateValues;
import com.arc.util.data.PageResult;
import com.atom.core.model.AdminUser;
import com.atom.core.model.AdminUserParam;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.arc.db.jsd.Shortcut.*;

/**
 * Created by md on 2020/07/25.
 */
@Repository
public class AdminUserDao extends BaseDao  {

	public PageResult<AdminUser> query(AdminUserParam adminUserParam) {
		PageResult<AdminUser> result = new PageResult<>();
		Filter filter= Filter.create();

        if(0<(adminUserParam.getType())){
            filter=filter.and(f("type",adminUserParam.getType()));
        }
        if(0<(adminUserParam.getIsEnable())){
            filter=filter.and(f("is_enable",adminUserParam.getIsEnable()));
        }
		if(!Strings.isNullOrEmpty(adminUserParam.getAccount())){
			filter=filter.and(f("account",adminUserParam.getAccount()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getPassword())){
			filter=filter.and(f("password",adminUserParam.getPassword()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getRole())){
			filter=filter.and(f("role",adminUserParam.getRole()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getEmail())){
			filter=filter.and(f("email",adminUserParam.getEmail()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getMobile())){
			filter=filter.and(f("mobile",adminUserParam.getMobile()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getWxId())){
			filter=filter.and(f("wx_id",adminUserParam.getWxId()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getAvatar())){
			filter=filter.and(f("avatar",adminUserParam.getAvatar()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getNickname())){
			filter=filter.and(f("nickname",adminUserParam.getNickname()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getRemark())){
			filter=filter.and(f("remark",adminUserParam.getRemark()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getQqAccount())){
			filter=filter.and(f("qq_account",adminUserParam.getQqAccount()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getWxAccount())){
			filter=filter.and(f("wx_account",adminUserParam.getWxAccount()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getWxQrcode())){
			filter=filter.and(f("wx_qrcode",adminUserParam.getWxQrcode()));
		}
		Sorters sorters = t("admin_user").sorters(SortType.DESC,"create_time");
		long total=(long)this.DB().select(count()).from("admin_user").where(filter).result().value();
		if(total>0){
			List<AdminUser> list = DB().select(AdminUser.class).where(filter).orderBy(sorters)
				.limit((adminUserParam.getPageIndex()-1)*adminUserParam.getPageSize(),adminUserParam.getPageSize())
				.result().all(AdminUser.class);
			result.setItems(list);
		}
		result.setTotalCount((int)total);
		return result;
	}

	public List<AdminUser> find(AdminUserParam adminUserParam) {
		Filter filter= Filter.create();

		if(!Strings.isNullOrEmpty(adminUserParam.getAccount())){
			filter=filter.and(f("account",adminUserParam.getAccount()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getPassword())){
			filter=filter.and(f("password",adminUserParam.getPassword()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getRole())){
			filter=filter.and(f("role",adminUserParam.getRole()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getEmail())){
			filter=filter.and(f("email",adminUserParam.getEmail()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getMobile())){
			filter=filter.and(f("mobile",adminUserParam.getMobile()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getWxId())){
			filter=filter.and(f("wx_id",adminUserParam.getWxId()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getAvatar())){
			filter=filter.and(f("avatar",adminUserParam.getAvatar()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getNickname())){
			filter=filter.and(f("nickname",adminUserParam.getNickname()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getRemark())){
			filter=filter.and(f("remark",adminUserParam.getRemark()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getQqAccount())){
			filter=filter.and(f("qq_account",adminUserParam.getQqAccount()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getWxAccount())){
			filter=filter.and(f("wx_account",adminUserParam.getWxAccount()));
		}
		if(!Strings.isNullOrEmpty(adminUserParam.getWxQrcode())){
			filter=filter.and(f("wx_qrcode",adminUserParam.getWxQrcode()));
		}
		List<AdminUser> list = DB().select(AdminUser.class)
			.where(filter).result().all(AdminUser.class);
		return list==null?new ArrayList<>():list;
	}

	public AdminUser get(String id) {
		AdminUser result = DB().select(AdminUser.class)
			.where(f("id",id)).result().one(AdminUser.class);
		return result;
	}

	public void add(AdminUser adminUser) {
		DB().insert(adminUser).result();
	}

	public void update(AdminUser adminUser) {
		UpdateValues updateValues = new UpdateValues();

		if(!Strings.isNullOrEmpty(adminUser.getAccount())){
			updateValues.add("account",adminUser.getAccount());
		}
		if(!Strings.isNullOrEmpty(adminUser.getPassword())){
			updateValues.add("password",adminUser.getPassword());
		}
		if(!Strings.isNullOrEmpty(adminUser.getRole())){
			updateValues.add("role",adminUser.getRole());
		}
		if(!Strings.isNullOrEmpty(adminUser.getEmail())){
			updateValues.add("email",adminUser.getEmail());
		}
		if(!Strings.isNullOrEmpty(adminUser.getMobile())){
			updateValues.add("mobile",adminUser.getMobile());
		}
		if(!Strings.isNullOrEmpty(adminUser.getWxId())){
			updateValues.add("wx_id",adminUser.getWxId());
		}
		if(!Strings.isNullOrEmpty(adminUser.getAvatar())){
			updateValues.add("avatar",adminUser.getAvatar());
		}
		if(!Strings.isNullOrEmpty(adminUser.getNickname())){
			updateValues.add("nickname",adminUser.getNickname());
		}
		if(!Strings.isNullOrEmpty(adminUser.getRemark())){
			updateValues.add("remark",adminUser.getRemark());
		}
		if(!Strings.isNullOrEmpty(adminUser.getQqAccount())){
			updateValues.add("qq_account",adminUser.getQqAccount());
		}
		if(!Strings.isNullOrEmpty(adminUser.getWxAccount())){
			updateValues.add("wx_account",adminUser.getWxAccount());
		}
		if(!Strings.isNullOrEmpty(adminUser.getWxQrcode())){
			updateValues.add("wx_qrcode",adminUser.getWxQrcode());
		}
		DB().update("admin_user").set(updateValues).where(f("id",adminUser.getId())).result();
	}

}