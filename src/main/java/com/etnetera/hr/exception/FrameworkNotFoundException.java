package com.etnetera.hr.exception;

public class FrameworkNotFoundException extends RuntimeException {

  public FrameworkNotFoundException(final String format) {
    super(format);
  }
}
