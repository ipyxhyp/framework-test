package com.etnetera.hr.utils;

import static java.util.stream.Collectors.toList;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.FrameworkDTO;

public class FrameworkUtils {


  public static JavaScriptFramework mapToJavascriptFramework(final FrameworkDTO dto) {
    final JavaScriptFramework javaScriptFramework = new JavaScriptFramework();
    javaScriptFramework.setName(dto.getName());
    javaScriptFramework.setHypeLevel(dto.getHypeLevel());
    javaScriptFramework.setVersions(
        dto.getVersions().stream().map(FrameworkVersion::new)
            .collect(toList()));
    return javaScriptFramework;
  }

  public static FrameworkDTO mapToFrameworkDto(final JavaScriptFramework javaScriptFramework) {
    final FrameworkDTO frameworkDTO = new FrameworkDTO();
    return copyToFrameworkDto(frameworkDTO, javaScriptFramework);
  }

  private static FrameworkDTO copyToFrameworkDto(final FrameworkDTO frameworkDTO,
      final JavaScriptFramework javaScriptFramework) {
    if (frameworkDTO != null && javaScriptFramework != null) {
      frameworkDTO.setId(javaScriptFramework.getId());
      frameworkDTO.setName(javaScriptFramework.getName());
      frameworkDTO.setHypeLevel(javaScriptFramework.getHypeLevel());
      frameworkDTO.setDeprecationDate(javaScriptFramework.getDeprecationDate());
      frameworkDTO.setVersions(
          javaScriptFramework.getVersions().stream().map(FrameworkVersion::getValue).collect(toList()));
    }
    return frameworkDTO;
  }

  public static JavaScriptFramework copyToFrameworkJavascript(final FrameworkDTO frameworkDTO,
      final JavaScriptFramework framework) {
    if (frameworkDTO != null && framework != null) {
      if (frameworkDTO.getHypeLevel() != null) {
        framework.setHypeLevel(frameworkDTO.getHypeLevel());
      }
      if (frameworkDTO.getName() != null) {
        framework.setName(frameworkDTO.getName());
      }
      if (frameworkDTO.getDeprecationDate() != null) {
        framework.setDeprecationDate(frameworkDTO.getDeprecationDate());
      }
    }
    return framework;
  }
}
