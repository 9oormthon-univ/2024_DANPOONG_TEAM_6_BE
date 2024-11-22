package com.example.festOn.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UrlProperties {

    @Value("${spring.url.redirectUrl.signUp}")
    private String signUpUrl;

    @Value("${spring.url.redirectUrl.main}")
    private String mainUrl;

    @Value("${spring.url.domain}")
    private String domain;
}
