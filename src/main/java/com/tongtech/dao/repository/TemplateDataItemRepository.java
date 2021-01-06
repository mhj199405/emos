package com.tongtech.dao.repository;


import com.tongtech.dao.entity.TemplateDataItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplateDataItemRepository
        extends JpaSpecificationExecutor<TemplateDataItem>,JpaRepository<TemplateDataItem, String> {


}
