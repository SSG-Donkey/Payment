package com.project.backend.mappers;

import com.project.backend.dto.KakaoApproveResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PaymentMapper {
    int insertPayment(KakaoApproveResponse kakaoApproveResponse);
}
