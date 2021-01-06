package com.tongtech.dao.repository.analysis;


import com.tongtech.dao.entity.analysis.UdaTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UdaThemeRepository extends JpaRepository<UdaTheme, Integer>, JpaSpecificationExecutor<UdaTheme> {
    UdaTheme findByThemeId(Integer themeId);
}
