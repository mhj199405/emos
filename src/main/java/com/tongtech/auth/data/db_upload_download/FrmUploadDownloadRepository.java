package com.tongtech.auth.data.db_upload_download;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FrmUploadDownloadRepository extends JpaSpecificationExecutor<FrmUploadDownload>, JpaRepository<FrmUploadDownload,Integer> {

    FrmUploadDownload findFirstByOriginName(String originName);
}
