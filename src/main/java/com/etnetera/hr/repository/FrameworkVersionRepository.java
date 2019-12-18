package com.etnetera.hr.repository;

import com.etnetera.hr.data.FrameworkVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FrameworkVersionRepository extends JpaRepository<FrameworkVersion, Long> {

  @Query(
      value = "SELECT FV.* FROM framework_version FV, javascript_framework JSF WHERE FV.framework_id = JSF.id AND FV.value = :versionValue",
      nativeQuery = true)
  FrameworkVersion findByVersionValue(@Param("versionValue") String versionValue);

}
