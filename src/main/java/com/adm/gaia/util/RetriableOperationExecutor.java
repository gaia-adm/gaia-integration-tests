package com.adm.gaia.util;

import java.util.function.Function;

public class RetriableOperationExecutor {

    public static <TResult> TResult execute(
            Function<Void, TResult> delegate,
            Function<Exception, Boolean> ignore,
            int sleepTime,
            int retries) {

        int count = 0;
        while (true) {
            try {
                return delegate.apply(null);
            } catch (Exception e) {
                if (!ignore.apply(e)) {
                    throw e;
                }
                if (count++ > retries) {
                    throw e;
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
        }
    }
}
