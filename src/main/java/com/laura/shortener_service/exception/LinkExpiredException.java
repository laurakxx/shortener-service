package com.laura.shortener_service.exception;

public class LinkExpiredException extends RuntimeException{
  public LinkExpiredException(String shortCode) {
    super("Link expired with that shortCode: "+ shortCode);
  }
}
