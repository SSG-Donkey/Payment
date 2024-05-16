package com.project.backend.controller;

import com.project.backend.dto.KakaoApproveResponse;
import com.project.backend.service.KakaoPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
@Log
public class    KakaoPayController {
    @Setter(onMethod_ = @Autowired)
    private KakaoPayService kakaoPay;

    //결제 요청
    @PostMapping("/ready")
    public void readyToKakaoPay(HttpServletResponse response, HttpServletRequest request) {
        log.info("payment/ready 진입");
        try {
            response.sendRedirect(kakaoPay.kakaoPayReady(request.getParameter("amount"),request.getParameter("user_nickname")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //결제 승인
    @PostMapping("/success")
    public KakaoApproveResponse afterPayRequest(@RequestBody String pgToken) {
        log.info("payment/success 진입");
        log.info("pgToken: " + pgToken);
        KakaoApproveResponse kakaoApprove = kakaoPay.kakaoPayApprove(pgToken);

        // 결제가 이루어진 경우 데이터베이스에 관련정보 입력
        if (kakaoApprove != null) {
            kakaoApprove.setTotal(kakaoApprove.getAmount().getTotal());
            kakaoPay.insert(kakaoApprove);
            log.info("insert 완료");
            kakaoPay.update();
        }
        return kakaoApprove;
    }

    //결제 중 취소
    @GetMapping("/cancel")
    public void cancel() {
        log.info("payment cancel");
        //throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
    }

    //결제 실패
    @GetMapping("/fail")
    public void fail() {
        log.info("payment failed");
        //throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
    }
}