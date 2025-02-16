package com.radzik.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class RequestData {
    public String model;
    public List<Message> messages;
}
