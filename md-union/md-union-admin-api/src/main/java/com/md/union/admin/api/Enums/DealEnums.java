package com.md.union.admin.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标维权
 */
public enum DealEnums {



    BRAND_REFUSE_DEAL(1, "D0001","商标驳回复审","商标注册被驳回后的唯一救济方式",1800,"http://47.92.65.35:8082/file/brand-power/1.png"),

    BRAND_DISCUSS_DEAL(2, "D0002","商标异议答辩","商标在公示期内被他人提出异议，可以申请异议答辩",2000,"http://47.92.65.35:8082/file/brand-power/2.png"),

    BRAND_CANCEL_DEAL(3, "D0003","商标撤三申请","撤销他人三年不使用商标，扫清障碍",1500,"http://47.92.65.35:8082/file/brand-power/3.png"),

    BRAND_OBJECTTION_DEAL(4, "D0004","商标异议申请","异议公示期近似商标，阻止其注册成功，维护品牌权益",1500,"http://47.92.65.35:8082/file/brand-power/4.png"),

    BRAND_MONITOR_DEAL(5, "D0005","侵权监测","拒绝搭便车，蹭热度，多个类别同时查询",2300,"http://47.92.65.35:8082/file/brand-power/5.png"),

    BRAND_APPEAL_DEAL(6, "D0006","电商申诉","针对店铺品牌，产品被投诉，侵权，提供一站式申诉服务",1600,"http://47.92.65.35:8082/file/brand-power/6.png"),

    BRAND_INVALID_DEAL(7, "D0007","商标无效宣告","宣告他人商标无效，扫清障碍",1200,"http://47.92.65.35:8082/file/brand-power/7.png"),

    BRAND_PERMIT_DEAL(8, "D0008","商标许可备案","不放弃商标权，但允许他人使用该商标",1200,"http://47.92.65.35:8082/file/brand-power/8.png"),
            ;

    DealEnums(int type, String no, String title, String brief, int price, String icon) {
        this.type = type;
        this.title = title;
        this.brief = brief;
        this.price = price;
        this.icon = icon;
        this.no = no;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public int getPrice() {
        return price;
    }

    public String getIcon() {
        return icon;
    }

    public String getNo() {
        return no;
    }

    private String def_img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595638504208&di=754606fdcb88e6b4ee9ea1476f4c2a5f&imgtype=0&src=http%3A%2F%2Fimage-ali.bianjiyi.com%2F1%2F2018%2F0710%2F14%2F15312043093853.jpg";

    //维权类型
    private int type;
    //标题
    private String title;
    //简介
    private String brief;
    //价格
    private int price;
    //图片
    private String icon;
    //产品编号
    private String no;

    public static DealEnums valueType(int id) {
        for (DealEnums item : DealEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标维权主键id不存在");
    }

    public static DealEnums valueNo(String no) {
        for (DealEnums item : DealEnums.values()) {
            if (item.no.equals(no)) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标维权主键id不存在");
    }

}
