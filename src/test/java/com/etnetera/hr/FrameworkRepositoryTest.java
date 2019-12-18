package com.etnetera.hr;

import static org.assertj.core.api.Assertions.assertThat;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.FrameworkRepository;
import com.etnetera.hr.repository.FrameworkVersionRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@EntityScan(basePackages = "com.etnetera.hr")
public class FrameworkRepositoryTest {

  private final String jQueryName = "jQueryFramework";
  private final String firstVersionValue = "1.0.0";

  @Autowired
  private FrameworkRepository frameworkRepository;

  @Autowired
  private FrameworkVersionRepository frameworkVersionRepository;


  @Test
  public void testCreateFrameworkOK() {
    // given
    // when
    JavaScriptFramework framework = createJavaScriptFramework(jQueryName, firstVersionValue);
    // then
    assertThat(framework.getVersions().get(0).getValue()).isEqualTo(firstVersionValue);
  }


  @Test
  public void testReadFrameworkByNameOK() {
    final String frameworkName = "Angular";
    JavaScriptFramework jQuery = new JavaScriptFramework(frameworkName);
    frameworkRepository.save(jQuery);
    JavaScriptFramework javaScriptFramework = frameworkRepository.findByName("name");
    assertThat(javaScriptFramework).isNull();
    javaScriptFramework = frameworkRepository.findByName(frameworkName);
    assertThat(javaScriptFramework).isNotNull();
    assertThat(javaScriptFramework.getVersions()).isNotNull().isEmpty();
  }

  @Test
  public void testUpdateFrameworkOK() {
    // given
    JavaScriptFramework framework = createJavaScriptFramework(jQueryName, firstVersionValue);
    // when
    framework.setDeprecationDate(LocalDate.now());
    FrameworkVersion frameworkVersion = frameworkVersionRepository
        .findByVersionValue(framework.getVersions().get(0).getValue());
    assertThat(frameworkVersion).isNotNull();
    assertThat(frameworkVersion.getValue()).isEqualTo(firstVersionValue);
    framework.removeVersion(frameworkVersion);
    frameworkRepository.saveAndFlush(framework);
    Optional<JavaScriptFramework> frameworkOptional = frameworkRepository
        .findById(framework.getId());
    assertThat(frameworkOptional.isPresent()).isTrue();
    framework = frameworkOptional.get();
    // then
    assertThat(framework.getVersions().isEmpty());
  }


  @Test
  public void testDeleteFrameworkOK() {
    // given
    JavaScriptFramework framework = createJavaScriptFramework(jQueryName, firstVersionValue);
    long id = framework.getId();
    // when
    frameworkRepository.delete(framework);
    // then
    Optional<JavaScriptFramework> deletedFramework = frameworkRepository.findById(id);
    assertThat(deletedFramework.isPresent()).isFalse();
  }

  private JavaScriptFramework createJavaScriptFramework(final String frameworkName,
      final String versionValue) {
    JavaScriptFramework jQueryFramework = new JavaScriptFramework(frameworkName);
    FrameworkVersion firstVersion = new FrameworkVersion(versionValue);
    jQueryFramework.addVersion(firstVersion);
    frameworkRepository.save(jQueryFramework);
    jQueryFramework = frameworkRepository.findByName(frameworkName);
    assertThat(jQueryFramework).isNotNull();
    assertThat(jQueryFramework).isEqualTo(jQueryFramework);
    assertThat(jQueryFramework.getVersions()).isNotNull().isNotEmpty();
    return jQueryFramework;
  }


}

