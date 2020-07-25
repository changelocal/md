package com.md.union.front.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标信息变更
 */
public enum ChangeEnums {



    BRAND_REFUSE_DEAL(1, "商标转让", "商标权发生改变，依照商标法规定须向商标局提", 700,""),
    BRAND_DISCUSS_DEAL(2, "商标宽展", "商标到期后6个月希望继续保有该商标，须向", 1200,""),
    BRAND_CANCEL_DEAL(3, "商标续展", "保护商标品牌，延长有效期", 980,""),
    BRAND_OBJECTTION_DEAL(4, "商标补证", "商标注册证遗失或破损，即刻为您申请补发证书", 800,""),
    BRAND_MONITOR_DEAL(5, "商标更正", "如注册申报时公司名称或地址填写错误等，须向", 400,""),
    BRAND_APPEAL_DEAL(6, "商标注销", "商标持有人放弃全部或部分商标权，须向商标局提出", 300,""),
    BRAND_INVALID_DEAL(7, "商标变更", "如需变更注册人名义，等级地址等注册事项，需向", 350,""),
    ;

    ChangeEnums(int type, String title, String brief, int price, String img) {
        this.type = type;
        this.title = title;
        this.brief = brief;
        this.price = price;
        this.img = img;
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

    public String getImg() {
        return img==null?def_img:img;
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
    private String img;

    public static ChangeEnums valueType(int id) {
        for (ChangeEnums item : ChangeEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标信息变更主键id不存在");
    }

}
