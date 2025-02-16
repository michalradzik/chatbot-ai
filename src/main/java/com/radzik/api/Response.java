package com.radzik.api;

import java.util.List;

public record Response(List<Choice> choices) {

    public record Choice(Message message) {
    }
}

