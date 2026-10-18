package com.digitalfix.digitalfix_notify.config;

public final class RabbitMQConfig {

    private RabbitMQConfig() {}

    public static final String EMAIL_QUEUE = "q.cmd.email";
    public static final String DISPATCH_QUEUE = "q.cmd.dispatch";
    public static final String REPORT_QUEUE = "q.cmd.report";
}
