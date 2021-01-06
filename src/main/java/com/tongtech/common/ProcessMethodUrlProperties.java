package com.tongtech.common;

import com.tongtech.common.vo.MethodForUrl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("process.method")
@Data
public class ProcessMethodUrlProperties {
    private List<MethodForUrl> methodList;
}
