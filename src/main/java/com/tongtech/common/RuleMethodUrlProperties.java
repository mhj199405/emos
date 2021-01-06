package com.tongtech.common;

import com.tongtech.common.vo.MethodForUrl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("rule.method")
@Data
public class RuleMethodUrlProperties {
    private List<MethodForUrl> methodList;

    public List<MethodForUrl> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<MethodForUrl> methodList) {
        this.methodList = methodList;
    }
}
