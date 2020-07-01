package com.md.union.front.api.controller;


import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.SampleDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("buy")
public class BuyTrademarkController {

    @Autowired
    private FrontClient frontClient;

    @GetMapping("/hot")
    public TrademarkDTO.HotResp hot() {

        TrademarkDTO.Hot req = new TrademarkDTO.Hot();
        BaseResponse<TrademarkDTO.HotResp> response = frontClient.hot(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }

    @PostMapping("/search")
    public TrademarkDTO.SearchResp search() {

        TrademarkDTO.Search req = new TrademarkDTO.Search();
        BaseResponse<TrademarkDTO.SearchResp> response = frontClient.search(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }
    @PostMapping("/consultation")
    public TrademarkDTO.ConsultationResp consultation() {

        TrademarkDTO.Consultation req = new TrademarkDTO.Consultation();
        BaseResponse<TrademarkDTO.ConsultationResp> response = frontClient.consultation(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }
    @PostMapping("/detail")
    public TrademarkDTO.PurchaseResp detail() {

        TrademarkDTO.Purchase req = new TrademarkDTO.Purchase();
        BaseResponse<TrademarkDTO.PurchaseResp> response = frontClient.detail(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }
    @PostMapping("/signup/detail")
    public TrademarkDTO.SignUpDetailsResp signup() {

        TrademarkDTO.SignUpDetails req = new TrademarkDTO.SignUpDetails();
        BaseResponse<TrademarkDTO.SignUpDetailsResp> response = frontClient.signup(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        return response.getResult();
    }
}
