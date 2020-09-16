package com.md.union.admin.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标维权
 */
public enum DealEnums {



    BRAND_REFUSE_DEAL(1, "7de5-495d-ab49-248238f48a8d-1d4c872533ca-d41d8","商标驳回复审","商标注册被驳回后的唯一救济方式",1800,360,"https://pay.mdlogo.cn/file/brand-power/1.png"),

    BRAND_DISCUSS_DEAL(2, "1b48-4473-874b-39a517423882-1d4c872533ca-d41d8","商标异议答辩","商标在公示期内被他人提出异议，可以申请异议答辩",2000,400,"https://pay.mdlogo.cn/file/brand-power/2.png"),

    BRAND_CANCEL_DEAL(3, "e017-43a1-baf0-2d40f9d749d2-1d4c872533ca-d41d8","商标撤三申请","撤销他人三年不使用商标，扫清障碍",1500,300,"https://pay.mdlogo.cn/file/brand-power/3.png"),

    BRAND_OBJECTTION_DEAL(4, "2f03-49a7-8ac2-cad776e771ea-1d4c872533ca-d41d8","商标异议申请","异议公示期近似商标，阻止其注册成功，维护品牌权益",1500,300,"https://pay.mdlogo.cn/file/brand-power/4.png"),

    BRAND_MONITOR_DEAL(5, "9dae-4db4-8e1a-93f8515c9791-1d4c872533ca-d41d8","侵权监测","拒绝搭便车，蹭热度，多个类别同时查询",2300,460,"https://pay.mdlogo.cn/file/brand-power/5.png"),

    BRAND_APPEAL_DEAL(6, "a82a-4cb0-a1c6-1774f77f3e76-1d4c872533ca-d41d8","电商申诉","针对店铺品牌，产品被投诉，侵权，提供一站式申诉服务",1600,320,"https://pay.mdlogo.cn/file/brand-power/6.png"),

    BRAND_INVALID_DEAL(7, "4c00-4a54-9a51-2ec3adb57454-1d4c872533ca-d41d8","商标无效宣告","宣告他人商标无效，扫清障碍",1200,240,"https://pay.mdlogo.cn/file/brand-power/7.png"),

    BRAND_PERMIT_DEAL(8, "0191-4909-b5ed-5a075ed2fdd4-1d4c872533ca-d41d8","商标许可备案","不放弃商标权，但允许他人使用该商标",1200,240,"https://pay.mdlogo.cn/file/brand-power/8.png"),
            ;

    DealEnums(int type, String no, String title, String brief, int price, int prepay,String icon) {
        this.type = type;
        this.title = title;
        this.brief = brief;
        this.price = price;
        this.prepay = prepay;
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
    public int getPrepay() {
        return prepay;
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
    private int prepay;
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
