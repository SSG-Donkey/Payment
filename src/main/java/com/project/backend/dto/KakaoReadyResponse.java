package com.project.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class KakaoReadyResponse {
    private String tid;
    private String next_redirect_pc_url;
    private Date created_at;
}
