package com.etnetera.hr.data;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "framework_version")
public class FrameworkVersion {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  @Column
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "framework_id")
  private JavaScriptFramework javaScriptFramework;

  @Column
  private LocalDate createDate;

  public FrameworkVersion() {
  }

  public FrameworkVersion(final String value) {
    this.value = value;
    this.createDate = LocalDate.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }
  
  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(final LocalDate createDate) {
    this.createDate = createDate;
  }

  public JavaScriptFramework getJavaScriptFramework() {
    return javaScriptFramework;
  }

  public void setJavaScriptFramework(final JavaScriptFramework javaScriptFramework) {
    this.javaScriptFramework = javaScriptFramework;
  }


  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FrameworkVersion)) {
      return false;
    }
    FrameworkVersion that = (FrameworkVersion) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(value, that.value) &&
        Objects.equals(createDate, that.createDate);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, value, createDate);
  }

  @Override
  public String toString() {
    return "FrameworkVersion{" +
        "id=" + id +
        ", value=" + value + '\'' +
        ", createDate=" + createDate +
        '}';
  }
}
