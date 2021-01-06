package com.tongtech.dao.repository;


import com.tongtech.dao.entity.CommonProcDataT;
import com.tongtech.dao.entity.CommonProcDataTPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommonProcDataTRepository extends JpaRepository<CommonProcDataT, CommonProcDataTPK> {

}