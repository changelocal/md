package com.md.union.front.client.feign;


import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.SampleDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务接口
 */
@FeignClient(name = "md-atom-user-service", path = "/api")
public interface FrontClient {

	/**
	 * 新增操作.
	 * 
	 * @param request 请求对象
	 * @return 响应对象
	 */
	@PostMapping(value = "/create", headers = { "Content-Type=application/json" })
	BaseResponse<SampleDTO.TestResp> create(@RequestBody SampleDTO.Test request);

    @PostMapping(value = "/hot", headers = { "Content-Type=application/json" })
    BaseResponse<TrademarkDTO.HotResp> hot(@RequestBody TrademarkDTO.Hot request);

}