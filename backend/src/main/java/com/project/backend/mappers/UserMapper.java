package com.project.backend.mappers;


import com.project.backend.dto.KakaoApproveResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int updatePoint( String partner_user_id, Integer point);
}