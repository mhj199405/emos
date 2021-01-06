package com.tongtech.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Data
public class StorageProperties {
    private String location = "upload-dir";
    private String locationDownload = "download-dir";
}
