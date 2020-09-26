package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.google.common.base.Strings;
import com.md.union.admin.api.vo.Consultation;
import com.md.union.front.client.dto.ConsultationDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/web/consultation")
@Api(tags = {"咨询"})
@Slf4j
public class ConsultationController {
    @Autowired
    private FrontClient frontClient;
    public static Date dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            oldDate= oldDate.replace("Z", " UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        return df2.format(date1);
        return date1;
    }
    @ApiOperation("商标起名查询")
    @PostMapping("/query")
    public Consultation.SearchRes query(@RequestBody Consultation.SearchReq request) {
        log.info("query:"+request);
        Consultation.SearchRes ret = new Consultation.SearchRes();
        ConsultationDTO.Info adminUser = new ConsultationDTO.Info();
        BeanUtils.copyProperties(request, adminUser);

        if(request.getDateRange()!=null && request.getDateRange().length>0
                && !Strings.isNullOrEmpty(request.getDateRange()[0])
                && !Strings.isNullOrEmpty(request.getDateRange()[1])){
            adminUser.setCreateTimeBegin(dealDateFormat(request.getDateRange()[0]));
            adminUser.setCreateTimeEnd(dealDateFormat(request.getDateRange()[1]));
        }else{
            adminUser.setCreateTimeBegin(null);
            adminUser.setCreateTimeEnd(null);
        }

        log.info("adminUser:"+adminUser);
        BaseResponse<ConsultationDTO.QueryResp> query = frontClient.query(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getInfos())){
            List<Consultation.Info> infos = new ArrayList<>();
            query.getResult().getInfos().forEach(p->{
                Consultation.Info info = new Consultation.Info();
                BeanUtils.copyProperties(p, info);

                TrademarkDTO.MdBrand mdBrand = new TrademarkDTO.MdBrand();
                mdBrand.setId(info.getOrderNo());
//                BaseResponse<TrademarkDTO.QueryResp> queryRespBaseResponse = frontClient.find(mdBrand);
//                if (!queryRespBaseResponse.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
//                    throw new ServiceException(queryRespBaseResponse.getStatus(), queryRespBaseResponse.getMessage());
//                }
//                if(!CollectionUtils.isEmpty(queryRespBaseResponse.getResult().getMdBrands())){
//                    info.setName(queryRespBaseResponse.getResult().getMdBrands().get(0).getBrandName());
//                }else{
//                    info.setName("无");
//                }
                if(p.getOrderNo().equals("000000")){
                    info.setName("首页咨询");
                }
                infos.add(info);
            });
            ret.setList(infos);
            ret.setCount(query.getResult().getTotal());
        }else{
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Consultation.Info request) {
        ConsultationDTO.Info info = new ConsultationDTO.Info();
        BeanUtils.copyProperties(request, info);
        info.setUpdateTime(new Date());
        BaseResponse<ConsultationDTO.Resp> query = frontClient.update(info);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

    }

}