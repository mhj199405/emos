package com.tongtech.dao.repository;

import com.tongtech.dao.entity.CommonKeyDictT;
import com.tongtech.dao.entity.CommonKeyDictTPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommonKeyDictTRepository extends JpaRepository<CommonKeyDictT, CommonKeyDictTPK> {

    @Query(value = "select t from CommonKeyDictT t where t.keyName = ?1")
    public List<CommonKeyDictT> findByKeyName(String name);

    @Query(value = "select max(t.keyId) from CommonKeyDictT t ")
    public Integer getMaxKeyId();
}
