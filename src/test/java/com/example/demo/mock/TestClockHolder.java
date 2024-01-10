package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import lombok.Builder;

@Builder
public record TestClockHolder(long millis) implements ClockHolder {
}
