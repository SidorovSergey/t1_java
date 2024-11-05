package ru.t1.java.demo.service;

public interface ErrorLogService {

    void logError(String message, String signature, String stackTrace);

}
