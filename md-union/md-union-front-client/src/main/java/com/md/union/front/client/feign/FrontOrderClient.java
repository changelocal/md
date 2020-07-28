package com.md.union.front.client.feign;


import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.RefDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务接口
 */
@FeignClient(name = "md-atom-order-service")
public interface FrontOrderClient {

	/**
	 * 新增操作.
	 * 
	 * @param request 请求对象
	 * @return 响应对象
	 */
	@PostMapping(value = "/ref/update", headers = { "Content-Type=application/json" })
	BaseResponse<RefDTO.Resp> update(@RequestBody RefDTO.OrderRefFile request);

    @PostMapping(value = "/ref/add", headers = { "Content-Type=application/json" })
    BaseResponse<RefDTO.Resp> add(@RequestBody RefDTO.OrderRefFile request);
}