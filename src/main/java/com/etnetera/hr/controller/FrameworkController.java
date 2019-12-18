package com.etnetera.hr.controller;


import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.exception.FrameworkNotFoundException;
import com.etnetera.hr.service.FrameworkService;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
@RequestMapping("/api/frameworks")
public class FrameworkController {

  public static final Logger logger = LoggerFactory.getLogger(FrameworkController.class);


  private final FrameworkService frameworkService;

  @Autowired
  public FrameworkController(FrameworkService service) {
    this.frameworkService = service;
  }

  @GetMapping("/all")
  public ResponseEntity<List<FrameworkDTO>> getAll() {
    List<FrameworkDTO> frameworkList = frameworkService.getJavascriptFrameworks();
    if (frameworkList.isEmpty()) {
      frameworkList = Collections
          .singletonList(new FrameworkDTO("test framework", null, 100500L, null));
    }
    return ResponseEntity.ok(frameworkList);
  }


  @PostMapping
  public ResponseEntity<URI> create(@RequestBody FrameworkDTO frameworkDTO) {
    final FrameworkDTO createdFrameworkDto = this.frameworkService.createFramework(frameworkDTO);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdFrameworkDto.getId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping
  public ResponseEntity<FrameworkDTO> getByName(@RequestParam String name)
      throws FrameworkNotFoundException {
    FrameworkDTO frameworkDTO = this.frameworkService.getJavascriptFrameworkByName(name);
    if (frameworkDTO == null) {
      // return stub for test
      frameworkDTO = new FrameworkDTO();
      frameworkDTO.setId(100500L);
      frameworkDTO.setName("test framework");
    }
    return ResponseEntity.ok(frameworkDTO);
  }

  @GetMapping("{id}")
  public ResponseEntity<FrameworkDTO> getFramework(@PathVariable("id") Long frameworkId)
      throws FrameworkNotFoundException {
    FrameworkDTO frameworkDTO = this.frameworkService.getJavascriptFramework(frameworkId);
    return ResponseEntity.ok(frameworkDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<FrameworkDTO> update(@PathVariable("id") Long id,
      @RequestBody FrameworkDTO frameworkDTO)
      throws FrameworkNotFoundException {
    FrameworkDTO updatedFramework = frameworkService.updateFramework(id, frameworkDTO);
    return ResponseEntity.ok(updatedFramework);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
    this.frameworkService.deleteFrameworkById(id);
    return ResponseEntity.ok(id);
  }

  @PostMapping("/{id}/version")
  public ResponseEntity<Boolean> addVersion(@PathVariable("id") Long id,
      @RequestBody String versionValue) throws FrameworkNotFoundException {
    Boolean isCreated = this.frameworkService.createFrameworkVersion(id, versionValue);
    if (isCreated) {
      return ResponseEntity.ok(isCreated);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(isCreated);
    }
  }

  @DeleteMapping("/{id}/version")
  public ResponseEntity<Boolean> removeVersion(@PathVariable("id") Long id,
      @RequestBody String versionValue) throws FrameworkNotFoundException {
    Boolean isDeleted = this.frameworkService.deleteFrameworkVersion(id, versionValue);
    if (isDeleted) {
      return ResponseEntity.ok(isDeleted);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(isDeleted);
    }
  }

  @PostMapping("/{id}/deprecate")
  public ResponseEntity<FrameworkDTO> deprecate(@PathVariable("id") Long id,
      @RequestBody(required = false) LocalDate deprecationDate) {
    final FrameworkDTO deprecatedFrameworkDto = this.frameworkService
        .deprecateFramework(id, deprecationDate);
    return ResponseEntity.ok(deprecatedFrameworkDto);
  }


}
