package com.order.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
public class CustomRetryListener implements RetryListener {

    private static final Logger log = LoggerFactory.getLogger(CustomRetryListener.class);

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        log.info("Retry operation started: {}", context.getAttribute("service failed"));
        return true; // proceed with retry
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.info("Retry operation ended: {} (attempts: {})", context.getAttribute("service failed"), context.getRetryCount());
    }

    @Override
    public <T, E extends Throwable> void onError(
            RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.warn("Retryable error occurred (attempt {}): {}",
                context.getRetryCount(), throwable.getMessage());
    }
}
