package com.digitalfix.digitalfix_notify.dto;

public record DispatchCommand(Long orderId, Long technicianId, String description) {
}
