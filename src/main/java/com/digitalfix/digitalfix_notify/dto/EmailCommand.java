package com.digitalfix.digitalfix_notify.dto;

public record EmailCommand(
    Long orderId,
    String recipient,
    String subject,
    String message
) {}
