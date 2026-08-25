package com.laura.shortener_service.exception;

public class LinkNotFoundException extends RuntimeException{//404
  public LinkNotFoundException() {super("Link not found");}
}
