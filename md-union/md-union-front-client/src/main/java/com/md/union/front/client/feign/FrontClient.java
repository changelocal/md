package com.md.union.front.client.feign;


import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务接口
 */
@FeignClient(name = "md-atom-user-service")
public interface FrontClient {

	/**
	 * 新增操作.
	 * 
	 * @param request 请求对象
	 * @return 响应对象
	 */
	@PostMapping(value = "/create", headers = { "Content-Type=application/json" })
	BaseResponse<SampleDTO.TestResp> create(@RequestBody SampleDTO.Test request);

    @GetMapping(value = "/hello")
    BaseResponse<String> hello();

	/**********************************品牌相关***********************************/
    /**
     * 获得热门
     * @return
     */
    @GetMapping(value = "/brand/hot")
    BaseResponse<TrademarkDTO.HotResp> hot();
    /**
     * 热门点击后
     * @return
     */
    @PostMapping(value = "/brand/hot/click", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.HotClickResp> hotClick();

    /**
     * md商标的翻页查询
     * @param request
     * @return
     */
    @PostMapping(value = "/mdbrand/query", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.QueryResp> search(@RequestBody TrademarkDTO.MdBrand request);
    /**
     * md商标的精确查询
     * @param request
     * @return
     */
    @PostMapping(value = "/mdbrand/find", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.QueryResp> find(@RequestBody TrademarkDTO.MdBrand request);

    /**
     * 点击咨询按钮随意返回有效的销售人员
     * @param request
     * @return
     */
    @PostMapping(value = "/adminuser/consultation", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.ConsultationResp> consultation(@RequestBody TrademarkDTO.Consultation request);

//    @PostMapping(value = "/brand/detail", headers = { "Content-Type=application/json" })
//    BaseResponse<TrademarkDTO.PurchaseResp> detail(@RequestBody TrademarkDTO.Purchase request);

    @PostMapping(value = "/brand/signup", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.SignUpDetailsResp> signup(@RequestBody TrademarkDTO.SignUpDetails request);

    @PostMapping(value = "/brand/right", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.SignUpDetailsResp> right(@RequestBody TrademarkDTO.SignUpDetails request);

    @GetMapping(value = "/brand/root", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.RootBrandResp> root();

    @GetMapping(value = "/brand/details/{code}", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.RootBrandResp> details(@PathVariable("code") int code);
    /**
     * 品牌起名搜索
     * @param request
     * @return
     */
    @PostMapping(value = "/name/search", headers = { "Content-Type=application/json" })
    BaseResponse<NameDTO.SearchRes> searchName(@RequestBody NameDTO.SearchReq request);
    /**********************************微信用户相关***********************************/
    @PostMapping(value = "/wxuser/add", headers = { "Content-Type=application/json" })
    BaseResponse<WxUserDTO.Resp> add(@RequestBody WxUserDTO.WxUser request);

    @PostMapping(value = "/wxuser/query", headers = { "Content-Type=application/json" })
    BaseResponse<WxUserDTO.QueryResp> query(@RequestBody WxUserDTO.WxUser request);

    /**********************************系统用户相关***********************************/
    @PostMapping(value = "/adminuser/add", headers = { "Content-Type=application/json" })
    BaseResponse<AdminUserDTO.Resp> add(@RequestBody AdminUserDTO.AdminUser request);

    @PostMapping(value = "/adminuser/query", headers = { "Content-Type=application/json" })
    BaseResponse<AdminUserDTO.QueryResp> query(@RequestBody AdminUserDTO.AdminUser request);

    @PostMapping(value = "/adminuser/find", headers = { "Content-Type=application/json" })
    BaseResponse<AdminUserDTO.QueryResp> find(@RequestBody AdminUserDTO.AdminUser request);

    @PostMapping(value = "/adminuser/update", headers = { "Content-Type=application/json" })
    BaseResponse<AdminUserDTO.Resp> update(@RequestBody AdminUserDTO.AdminUser request);

    /**********************************service***********************************/
    @GetMapping(value = "/service/get/{code}", headers = { "Content-Type=application/json" })
    BaseResponse<ServiceDTO.Service> getService(@PathVariable("code") String code);

    @PostMapping(value = "/service/find", headers = { "Content-Type=application/json" })
    BaseResponse<ServiceDTO.FindResp> findService(@RequestBody ServiceDTO.Service service);

    @PostMapping(value = "/service/add/consultation", headers = { "Content-Type=application/json" })
    BaseResponse<ServiceDTO.Resp> addConsultation(@RequestBody ServiceDTO.Consultation consultation );
}