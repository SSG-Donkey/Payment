package com.project.backend.service;

import com.project.backend.dto.KakaoApproveResponse;
import com.project.backend.dto.KakaoReadyResponse;
import com.project.backend.mappers.PaymentMapper;
import com.project.backend.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Log
public class KakaoPayService {
    static final String cid = "TC0ONETIME";
    static final String host = "https://open-api.kakaopay.com";
    @Value("${SECRET_KEY}") //보안상의 이유로 application.yml파일에서 관리
    String kakaoAdminKey;
    private KakaoReadyResponse kakaoPayReady;
    private KakaoApproveResponse kakaoPayApprove;
    private final PaymentMapper paymentmapper;
    private final UserMapper UserMapper;
    private String userNickname;
    private String pluspoint;

    public String kakaoPayReady(String amount,String usernickname) { //결제 준비
        // Server Request Body : 서버 요청 본문
        userNickname=usernickname;
        pluspoint=amount;
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid); // 가맹점 코드 - 테스트용
        params.put("partner_order_id", "1001"); // 주문 번호
        params.put("partner_user_id", usernickname); // 회원 아이디
        params.put("item_name", "포인트"); // 상품 명
        params.put("quantity", amount); // 상품 수량
        params.put("total_amount", amount); // 상품 총액
        params.put("tax_free_amount", "0"); // 상품 비과세 금액
        params.put("approval_url", "http://localhost:8080/paymentSuccess.html");
        params.put("cancel_url", "http://localhost:8080/kakao/cancel");
        params.put("fail_url", "http://localhost:8080/kakao/fail");
        log.info("parameter value : " + params);

        // 헤더와 바디 붙이기
        HttpEntity<Map<String, String>> body = new HttpEntity<Map<String, String>>(params, this.getHeaders());
        // URL 생성 및 전송
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("kakaoPayReady try 진입 ");
            kakaoPayReady = restTemplate.postForObject(new URI(host + "/online/v1/payment/ready"), body, KakaoReadyResponse.class);
        } catch (RestClientException e) {
            log.info("kakaoPayReady.RestClientException : " + e);
        } catch (URISyntaxException e) {
            log.info("kakaoPayReady.URISyntaxException : " + e);
        }
        return kakaoPayReady.getNext_redirect_pc_url();
    }

    public KakaoApproveResponse kakaoPayApprove(String pgToken) { //결제 승인 요청
        log.info("결제 승인요청 중 ........");
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid); //가맹점 코드 - 테스트용
        params.put("tid", kakaoPayReady.getTid()); //결제 고유번호 - kakaoPayReady() 실행시 생성
        params.put("partner_order_id", "1001"); // 주문번호
        params.put("partner_user_id", userNickname); // 회원 아이디
        params.put("pg_token", pgToken); //TID와 같이 생성된 인증 토큰
        log.info("parameter value : " + params);

        // 헤더와 바디 붙이기
        HttpEntity<Map<String, String>> body = new HttpEntity<Map<String, String>>(params, this.getHeaders());
        // URL 생성 및 전송
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("kakaoPayApprove try 진입 ");
            kakaoPayApprove = restTemplate.postForObject(new URI(host + "/online/v1/payment/approve"), body, KakaoApproveResponse.class);
        } catch (RestClientException e) {
            log.info("RestClientException : " + e);
        } catch (URISyntaxException e) {
            log.info("URISyntaxException : " + e);
        }
        return kakaoPayApprove;
    }

    // Server Request Header : 서버 요청 헤더
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", host);
        headers.add("Authorization", "SECRET_KEY " + kakaoAdminKey);
        headers.add("Content-Type", "application/json;charset=UTF-8");
        log.info("header value : " + headers);
        return headers;
    }

    public int insert(KakaoApproveResponse kakaoApproveResponse) {
        return paymentmapper.insertPayment(kakaoApproveResponse);
    }

    public int update(){
        int point = Integer.parseInt(pluspoint);

        return UserMapper.updatePoint(userNickname,point);
    }
}