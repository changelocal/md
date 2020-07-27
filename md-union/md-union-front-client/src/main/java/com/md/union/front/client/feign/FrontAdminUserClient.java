package com.md.union.front.client.feign;


import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.AdminUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务接口
 */
@FeignClient(name = "md-atom-user-service", path = "/adminuser")
public interface FrontAdminUserClient {

	/**
	 * 新增操作.
	 * 
	 * @param request 请求对象
	 * @return 响应对象
	 */
	@PostMapping(value = "/add", headers = { "Content-Type=application/json" })
	BaseResponse<AdminUserDTO.Resp> add(@RequestBody AdminUserDTO.AdminUser request);

    @PostMapping(value = "/query", headers = { "Content-Type=application/json" })
    BaseResponse<AdminUserDTO.QueryResp> query(@RequestBody AdminUserDTO.AdminUser request);

    @PostMapping(value = "/update", headers = { "Content-Type=application/json" })
    BaseResponse<AdminUserDTO.Resp> update(@RequestBody AdminUserDTO.AdminUser request);
}