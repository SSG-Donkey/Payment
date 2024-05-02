package com.project.backend.controller;
//import io.micrometer.common.util.StringUtils;

import com.project.backend.service.KakaoPayService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Log
public class PaymentController {
    //결제 페이지
    @Setter(onMethod_ = @Autowired)
    KakaoPayService kakapay;
    @PostMapping("")
    public String preparePayment(@RequestParam String amount, String paymentMethod, HttpServletResponse response) {
        if ("kakaopay".equals(paymentMethod)) {
            log.info("kakaopay 결제...........");
            try {
                response.sendRedirect(kakapay.kakaoPayReady(amount));
            } catch (IOException e) {
                log.info("RuntimeException : " + e);
            }
        }
        if ("tosspay".equals(paymentMethod)) {

            return "tosspay";
        }
        // 아임포트 결제 준비 API 호출
        // 결제 준비 정보 (결제 토큰 등)를 응답으로 받습니다.
        return "success"; // 결제 요청 페이지 URL
    }
    //결제 수단 = 네이버 페이

    //결제 수단 = 카카오 페이

    //결제 수단 = 토스 페이
    @PostMapping("/toss")
    public String tossPayment(String amount, String paymentMethod) {

        System.out.print("결제완료요청 \n");
        System.out.printf("%s, %s\n", amount, paymentMethod);


        return "success"; // 결제 요청 페이지 URL
    }
}