package com.etnetera.hr.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class FrameworkDTO {

  private Long id;
  private String name;
  private List<String> versions;
  private Long hypeLevel;
  private LocalDate deprecationDate;

  public FrameworkDTO() {
  }

  public FrameworkDTO(final String name, final List<String> version, final Long hypeLevel,
      final LocalDate deprecationDate) {
    this.name = name;
    this.versions = version;
    this.hypeLevel = hypeLevel;
    this.deprecationDate = deprecationDate;
  }

  @Override
  public String toString() {
    return "FrameworkDTO{" +
        "id='" + getId() + '\'' +
        "name='" + name + '\'' +
        ", versions='" + versions + '\'' +
        ", hypeLevel='" + hypeLevel + '\'' +
        ", deprecationDate=" + deprecationDate +
        '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FrameworkDTO)) {
      return false;
    }
    FrameworkDTO that = (FrameworkDTO) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(versions, that.versions) &&
        Objects.equals(getId(), that.getId()) &&
        Objects.equals(hypeLevel, that.hypeLevel) &&
        Objects.equals(deprecationDate, that.deprecationDate);
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId(), name, versions, hypeLevel, deprecationDate);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<String> getVersions() {
    return versions;
  }

  public void setVersions(final List<String> versions) {
    this.versions = versions;
  }

  public Long getHypeLevel() {
    return hypeLevel;
  }

  public void setHypeLevel(final Long hypeLevel) {
    this.hypeLevel = hypeLevel;
  }

  public LocalDate getDeprecationDate() {
    return deprecationDate;
  }

  public void setDeprecationDate(final LocalDate deprecationDate) {
    this.deprecationDate = deprecationDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
