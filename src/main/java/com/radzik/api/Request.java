package com.radzik.api;

import java.util.List;

public record Request(String model, List<Message> messages) {

}

