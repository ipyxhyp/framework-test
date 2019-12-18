package com.etnetera.hr;

import static org.assertj.core.api.Assertions.assertThat;

import com.etnetera.hr.dto.FrameworkDTO;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JavaScriptFrameworkTests {


  private static final String GET_ALL_URL = "/api/frameworks/all";
  public static final String API_FRAMEWORK_URL = "/api/frameworks";
  public static final String HTTP_LOCALHOST = "http://localhost:";

  private final HttpHeaders headers = new HttpHeaders();
  private final StringBuilder urlTemplate = new StringBuilder(HTTP_LOCALHOST);
  private String targetUrl;

  @LocalServerPort
  private int PORT;

  @Autowired
  private TestRestTemplate restTemplate;


  @Before
  public void setup() {
    this.targetUrl = this.urlTemplate.append(PORT).append(API_FRAMEWORK_URL).toString();
    this.headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testGetFrameworksOK() {
    getAllFrameworks();
  }

  @Test
  public void testGetByNameFails() {
    String URL = HTTP_LOCALHOST + PORT + API_FRAMEWORK_URL;
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
    HttpEntity<FrameworkDTO> httpEntity = new HttpEntity<>(this.headers);
    String name = "test";
    builder.queryParam("name", name);
    ResponseEntity<FrameworkDTO> responseEntity = this.restTemplate
        .exchange(builder.toUriString(), HttpMethod.GET, httpEntity, FrameworkDTO.class);

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    assertThat(responseEntity.getBody()).isNotNull();
  }

  @Test
  public void testGetFrameworkByIdOK() {
    ResponseEntity<FrameworkDTO> frameworkDTOResponseEntity;
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    List<String> location = verifyLocationResponse(responseEntity);
    String getUrl = location.get(0);
    Long id = getIdFromLocation(location);

    frameworkDTOResponseEntity = getFrameworkDTOResponseEntity(
        id, getUrl);
    assertThat(frameworkDTOResponseEntity).isNotNull();
    assertThat(frameworkDTOResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(frameworkDTOResponseEntity.getBody().getId()).isEqualTo(id);
  }

  @Test
  public void testCreateFrameworkOK() {
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    verifyLocationResponse(responseEntity);
  }

  @Test
  public void testDeleteFrameworkFails() {
    String targetUrl = this.urlTemplate.append("/{id}").toString();
    Long id = 100500L;
    ResponseEntity<?> result = this.restTemplate.exchange(targetUrl,
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Object.class,
        id);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCreateAndDeleteFrameworkOK() {
    String path = "/{id}";
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    Long id = getIdFromLocation(verifyLocationResponse(responseEntity));
    String deleteUrl = this.urlTemplate.append(path).toString();

    ResponseEntity<?> result = this.restTemplate.exchange(deleteUrl,
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Long.class,
        id);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
  }

  @Test
  public void testCreateAndUpdateFrameworkOK() {
    String path = "/{id}";
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    Long id = getIdFromLocation(verifyLocationResponse(responseEntity));
    String updateUrl = this.urlTemplate.append(path).toString();

    HttpEntity<FrameworkDTO> httpEntityRequest = getFrameworkDtoRequest("updated test Framework");

    ResponseEntity<FrameworkDTO> result = this.restTemplate.exchange(updateUrl,
        HttpMethod.PUT,
        httpEntityRequest,
        FrameworkDTO.class,
        id);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getName()).isEqualTo("updated test Framework");
  }


  @Test
  public void testAddVersionFrameworkOK() {
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    Long id = getIdFromLocation(verifyLocationResponse(responseEntity));
    String versionUrl = this.urlTemplate.append("/{id}/version").toString();

    String versionNextValue = "x.Y.z";
    HttpEntity<String> request = new HttpEntity<>(versionNextValue);
    ResponseEntity<Boolean> result = this.restTemplate.exchange(versionUrl,
        HttpMethod.POST,
        request,
        Boolean.class,
        id);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isTrue();
  }

  @Test
  public void testRemoveVersionFrameworkOK() {
    ResponseEntity<FrameworkDTO> frameworkDTOResponseEntity;
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    final Long id = getIdFromLocation(verifyLocationResponse(responseEntity));
    String getUrl = this.urlTemplate.append("/{id}").toString();
    String versionUrl = this.urlTemplate.append("/version").toString();
    String versionNextValue = "x.Y.z";
    FrameworkDTO frameworkDTO;
    HttpEntity<String> request = new HttpEntity<>(versionNextValue);
    // create version
    ResponseEntity<Boolean> result = this.restTemplate.exchange(versionUrl,
        HttpMethod.POST,
        request,
        Boolean.class,
        id);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isTrue();
    // get framework and check if version exists
    frameworkDTOResponseEntity = getFrameworkDTOResponseEntity(id,
        getUrl);
    assertThat(frameworkDTOResponseEntity).isNotNull();
    frameworkDTO = frameworkDTOResponseEntity.getBody();
    assertThat(frameworkDTO).isNotNull();
    assertThat(frameworkDTO.getVersions()).isNotNull().isNotEmpty();
    assertThat(frameworkDTO.getVersions().contains(versionNextValue)).isTrue();

    // delete version
    ResponseEntity<Boolean> deletionResult = this.restTemplate.exchange(versionUrl,
        HttpMethod.DELETE,
        request,
        Boolean.class,
        id);
    assertThat(deletionResult).isNotNull();
    assertThat(deletionResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(deletionResult.getBody()).isTrue();
    // call again and check if version is not exists anymore 
    frameworkDTOResponseEntity = getFrameworkDTOResponseEntity(id,
        getUrl);
    assertThat(frameworkDTOResponseEntity).isNotNull();
    assertThat(frameworkDTOResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    frameworkDTO = frameworkDTOResponseEntity.getBody();
    assertThat(frameworkDTO).isNotNull();
    assertThat(frameworkDTO.getId()).isEqualTo(id);
    assertThat(frameworkDTO.getVersions()).isNotNull();
    assertThat(frameworkDTO.getVersions().contains(versionNextValue)).isFalse();

  }

  @Test
  public void testDeprecateFrameworkOK() {
    // create framework
    FrameworkDTO frameworkDTO;
    ResponseEntity<FrameworkDTO> frameworkDTOResponseEntity;
    ResponseEntity<String> responseEntity = postCreateFramework(this.targetUrl);
    final Long id = getIdFromLocation(verifyLocationResponse(responseEntity));
    String getUrl = this.urlTemplate.append("/{id}").toString();
    String deprecateUrl = this.urlTemplate.append("/deprecate").toString();
    // read new framework
    frameworkDTOResponseEntity = getFrameworkDTOResponseEntity(id,
        getUrl);
    assertThat(frameworkDTOResponseEntity).isNotNull();
    assertThat(frameworkDTOResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    frameworkDTO = frameworkDTOResponseEntity.getBody();
    // and check that deprecation date is null
    assertThat(frameworkDTO).isNotNull();
    assertThat(frameworkDTO.getId()).isEqualTo(id);
    assertThat(frameworkDTO.getDeprecationDate()).isNull();
    // call deprecate endpoint and check if returned deprecationDate is equal to currentDate
    LocalDate currentDate = LocalDate.now();
    HttpEntity<LocalDate> httpLocalDateEntity = new HttpEntity<>(currentDate, this.headers);
    frameworkDTOResponseEntity = this.restTemplate
        .postForEntity(deprecateUrl, httpLocalDateEntity, FrameworkDTO.class, id);
    frameworkDTO = frameworkDTOResponseEntity.getBody();
    assertThat(frameworkDTO).isNotNull();
    assertThat(frameworkDTO.getId()).isEqualTo(id);
    assertThat(frameworkDTO.getDeprecationDate()).isEqualTo(currentDate);
  }

  private ResponseEntity<FrameworkDTO> getFrameworkDTOResponseEntity(final Long id,
      final String getUrl) {
    return this.restTemplate.exchange(getUrl,
        HttpMethod.GET,
        HttpEntity.EMPTY,
        FrameworkDTO.class,
        id);
  }

  private ResponseEntity<String> postCreateFramework(final String URL) {
    HttpEntity<FrameworkDTO> httpEntityRequest = getFrameworkDtoRequest("test framework");

    return this.restTemplate
        .postForEntity(URL, httpEntityRequest, String.class);
  }

  private HttpEntity<FrameworkDTO> getFrameworkDtoRequest(final String name) {
    FrameworkDTO requestFrameworkEntity = new FrameworkDTO(name, Collections.EMPTY_LIST,
        null, null);

    return new HttpEntity<>(requestFrameworkEntity, this.headers);
  }

  private List<String> verifyLocationResponse(final ResponseEntity<String> responseEntity) {
    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    List<String> location = responseEntity.getHeaders().get("Location");
    assertThat(location).isNotEmpty();
    assertThat(location.get(0)).isNotBlank();
    return location;
  }

  private long getIdFromLocation(final List<String> location) {
    String[] splitLocation = location.get(0).split("/");
    assertThat(splitLocation[splitLocation.length - 1]).isNotBlank();
    return Long.valueOf(splitLocation[splitLocation.length - 1]);
  }

  private void getAllFrameworks() {
    String allFrameworksUrl = HTTP_LOCALHOST + PORT + GET_ALL_URL;
    ResponseEntity<List> responseEntity = this.restTemplate.getForEntity(
        allFrameworksUrl,
        List.class);
    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isNotEmpty();
    responseEntity.getBody();
  }
}
