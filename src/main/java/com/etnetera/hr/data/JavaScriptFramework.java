package com.etnetera.hr.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
@Table(name = "javascript_framework")
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@OneToMany(mappedBy = "javaScriptFramework", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FrameworkVersion> versions = new ArrayList<>();

	@Column
	private Long hypeLevel;

	@Column
	private LocalDate deprecationDate;

	public JavaScriptFramework() {
	}

	public JavaScriptFramework(String name) {
		this.name = name;
	}

	public boolean addVersion(final FrameworkVersion version){
		version.setJavaScriptFramework(this);
		return versions.add(version);
	}

	public boolean removeVersion(final FrameworkVersion version){
		version.setJavaScriptFramework(null);
		return versions.remove(version);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FrameworkVersion> getVersions() {
		return versions;
	}

	public void setVersions(final List<FrameworkVersion> versions) {
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof JavaScriptFramework)) {
			return false;
		}
		JavaScriptFramework that = (JavaScriptFramework) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(versions, that.versions) &&
				Objects.equals(hypeLevel, that.hypeLevel) &&
				Objects.equals(deprecationDate, that.deprecationDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, versions, hypeLevel, deprecationDate);
	}

	@Override
	public String toString() {
		return "JavaScriptFramework{" +
				"id=" + id +
				", name='" + name + '\'' +
				", versions=" + versions +
				", hypeLevel=" + hypeLevel +
				", deprecationDate=" + deprecationDate +
				'}';
	}

}
