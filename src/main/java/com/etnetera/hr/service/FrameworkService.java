package com.etnetera.hr.service;

import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.exception.FrameworkNotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface FrameworkService {

  FrameworkDTO createFramework(FrameworkDTO frameworkDTO);

  void deleteFrameworkByName(String frameworkName) throws FrameworkNotFoundException;

  void deleteFrameworkById(Long frameworkId) throws FrameworkNotFoundException;

  FrameworkDTO updateFramework(FrameworkDTO frameworkDTO) throws FrameworkNotFoundException;

  FrameworkDTO updateFramework(Long id, FrameworkDTO frameworkDTO) throws FrameworkNotFoundException;

  List<FrameworkDTO> getJavascriptFrameworks();

  FrameworkDTO getJavascriptFramework(Long frameworkId);

  FrameworkDTO getJavascriptFrameworkByName(final String name) throws FrameworkNotFoundException;

  Boolean createFrameworkVersion(Long frameworkId, String version) throws FrameworkNotFoundException;

  Boolean deleteFrameworkVersion(Long frameworkId, String version) throws FrameworkNotFoundException;

  FrameworkDTO deprecateFramework(Long id, LocalDate deprecationDate);
}
