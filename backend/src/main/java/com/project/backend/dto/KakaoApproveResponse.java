package com.project.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Data
public class KakaoApproveResponse {

    private String aid;                 // 요청 고유번호
    private String tid;                 // 결제 고유번호
    private String cid;                 // 가맹점 코드
    private String partner_order_id;    // 가맹점 주문번호
    private String partner_user_id;     // 회원 아이디
    private String item_name;           // 상품 번호
    private String amount;               // 수량
    private Date created_at;            // 결제 요청 준비 시각
    private Date approved_at;           // 결제 승인 시각

}