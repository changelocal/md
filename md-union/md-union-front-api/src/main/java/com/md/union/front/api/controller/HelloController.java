package com.md.union.front.api.controller;


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
    public String hello() {
        return "hello";

        /*SampleDTO.Test req = new SampleDTO.Test();
        req.setId("100");
        req.setName("hello");
        BaseResponse<SampleDTO.TestResp> response = frontClient.create(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();*/
    }

}
