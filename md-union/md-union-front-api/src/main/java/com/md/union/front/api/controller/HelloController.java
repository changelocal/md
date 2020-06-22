package com.md.union.front.api.controller;


import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.SampleDTO;
import com.md.union.front.client.feign.FrontClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class HelloController {

    @Autowired
    private FrontClient frontClient;

    @GetMapping("/hello")
    public SampleDTO.TestResp hello() {

        SampleDTO.Test req = new SampleDTO.Test();
        req.setId("100");
        req.setName("hello");
        BaseResponse<SampleDTO.TestResp> response = frontClient.create(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }
    @GetMapping("/test")
    public SampleDTO.TestResp test() {

        SampleDTO.Test req = new SampleDTO.Test();
        req.setId("100");
        req.setName("hello");
        BaseResponse<SampleDTO.TestResp> response = frontClient.create(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }
}
