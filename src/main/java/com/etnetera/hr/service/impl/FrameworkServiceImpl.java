package com.etnetera.hr.service.impl;

import static java.util.stream.Collectors.toList;

import com.etnetera.hr.utils.FrameworkUtils;
import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.exception.FrameworkNotFoundException;
import com.etnetera.hr.repository.FrameworkRepository;
import com.etnetera.hr.repository.FrameworkVersionRepository;
import com.etnetera.hr.service.FrameworkService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("frameworkService")
public class FrameworkServiceImpl implements FrameworkService {

  private static final String FRAMEWORK_NOT_FOUND = "Framework %s not found ";
  private static final String FRAMEWORK_VERSION_NOT_FOUND = "Framework version %s not found ";

  @Autowired
  private FrameworkRepository frameworkRepository;

  @Autowired
  private FrameworkVersionRepository versionRepository;


  public void setFrameworkRepository(final FrameworkRepository frameworkRepository) {
    this.frameworkRepository = frameworkRepository;
  }

  public void setVersionRepository(final FrameworkVersionRepository versionRepository) {
    this.versionRepository = versionRepository;
  }

  @Override
  public FrameworkDTO getJavascriptFramework(Long frameworkId){
    JavaScriptFramework framework = this.frameworkRepository.findById(frameworkId).orElseThrow(
        () -> new FrameworkNotFoundException(String.format("Framework %s not found ", frameworkId)));
    return FrameworkUtils.mapToFrameworkDto(framework);
  }


  @Override
  public List<FrameworkDTO> getJavascriptFrameworks() {
    List<JavaScriptFramework> javaScriptFrameworks = this.frameworkRepository.findAll();
    List<FrameworkDTO> frameworkDTOList = Collections.emptyList();
    if (!javaScriptFrameworks.isEmpty()) {
      javaScriptFrameworks.stream().map(FrameworkUtils::mapToFrameworkDto)
          .collect(toList());
    }
    return frameworkDTOList;
  }

  public FrameworkDTO getJavascriptFrameworkByName(final String name)
      throws FrameworkNotFoundException {
    FrameworkDTO frameworkDTO = null;
    if (name != null && !name.isEmpty()) {
      JavaScriptFramework javaScriptFramework = this.frameworkRepository.findByName(name);
      if (javaScriptFramework != null) {
        frameworkDTO = FrameworkUtils.mapToFrameworkDto(javaScriptFramework);
      } else {
        throw new FrameworkNotFoundException(String.format("Framework %s not found ", name));
      }
    }
    return frameworkDTO;
  }

  @Override
  public FrameworkDTO createFramework(final FrameworkDTO frameworkDTO) {
    if (frameworkDTO != null) {
      JavaScriptFramework framework = FrameworkUtils.mapToJavascriptFramework(frameworkDTO);
      framework = this.frameworkRepository.save(framework);
      frameworkDTO.setId(framework.getId());
    }
    return frameworkDTO;
  }

  @Override
  public void deleteFrameworkByName(final String frameworkName) throws FrameworkNotFoundException {
    if (frameworkName != null && !frameworkName.isEmpty()) {
      JavaScriptFramework framework = this.frameworkRepository.findByName(frameworkName);
      if (framework != null) {
        this.frameworkRepository.delete(framework);
      } else {
        throw new FrameworkNotFoundException(
            String.format("Framework %s not found ", frameworkName));

      }
    }
  }

  @Override
  public void deleteFrameworkById(final Long frameworkId) throws FrameworkNotFoundException {
    JavaScriptFramework framework = this.frameworkRepository.findById(frameworkId).orElseThrow(
        () -> new FrameworkNotFoundException(
            String.format("Framework %s not found ", frameworkId)));
    this.frameworkRepository.delete(framework);
  }

  @Override
  public FrameworkDTO updateFramework(final FrameworkDTO frameworkDTO)
      throws FrameworkNotFoundException {
    JavaScriptFramework framework;
    framework = this.frameworkRepository.findByName(frameworkDTO.getName());
    if (framework != null) {
      this.frameworkRepository
          .save(FrameworkUtils.copyToFrameworkJavascript(frameworkDTO, framework));
    } else {
      throw new FrameworkNotFoundException(
          String.format("Framework %s not found ", frameworkDTO.getName()));
    }
    return frameworkDTO;
  }

  @Override
  public FrameworkDTO updateFramework(final Long id, final FrameworkDTO frameworkDTO)
      throws FrameworkNotFoundException {
    JavaScriptFramework javaScriptFramework = this.frameworkRepository.findById(id).orElseThrow(
        () -> new FrameworkNotFoundException(
            String.format("Framework %s not found ", frameworkDTO.getName())));
    this.frameworkRepository
        .saveAndFlush(FrameworkUtils.copyToFrameworkJavascript(frameworkDTO, javaScriptFramework));
    frameworkDTO.setId(id);
    return frameworkDTO;
  }

  @Override
  public Boolean createFrameworkVersion(Long frameworkId, String version)
      throws FrameworkNotFoundException {
    boolean isCreated;
    JavaScriptFramework framework = this.frameworkRepository.findById(frameworkId)
        .orElseThrow(() -> new FrameworkNotFoundException(String.format(
            FRAMEWORK_NOT_FOUND, frameworkId)));
    isCreated = framework.addVersion(new FrameworkVersion(version));
    this.frameworkRepository.saveAndFlush(framework);
    return isCreated;
  }

  @Override
  public Boolean deleteFrameworkVersion(Long frameworkId, String version)
      throws FrameworkNotFoundException {
    boolean isDeleted;
    JavaScriptFramework framework = this.frameworkRepository.findById(frameworkId).orElseThrow(
        () -> new FrameworkNotFoundException(
            String.format(FRAMEWORK_VERSION_NOT_FOUND, frameworkId)));
    FrameworkVersion frameworkVersion = this.versionRepository.findByVersionValue(version);
    if (frameworkVersion != null) {
      isDeleted = framework.removeVersion(frameworkVersion);
      this.frameworkRepository.saveAndFlush(framework);
    } else {
      throw new FrameworkNotFoundException(String.format(FRAMEWORK_VERSION_NOT_FOUND, frameworkId));
    }
    return isDeleted;
  }

  @Override
  public FrameworkDTO deprecateFramework(final Long frameworkId, final LocalDate deprecationDate) {
    LocalDate actualDeprecationDate = deprecationDate == null ? LocalDate.now() : deprecationDate;
    JavaScriptFramework javaScriptFramework = this.frameworkRepository.findById(frameworkId).orElseThrow(
        () -> new FrameworkNotFoundException(
            String.format("Framework %d not found ", frameworkId)));
    javaScriptFramework.setDeprecationDate(actualDeprecationDate);
    javaScriptFramework = this.frameworkRepository.saveAndFlush(javaScriptFramework);
    return FrameworkUtils.mapToFrameworkDto(javaScriptFramework);
  }

}
