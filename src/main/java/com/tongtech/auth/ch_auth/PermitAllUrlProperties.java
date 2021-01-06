package com.tongtech.auth.ch_auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("permit-all-url")
@Data
public class PermitAllUrlProperties {
	private List<String> url=new ArrayList<>();

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}
}