package cn.starlight.nightmare.util.world;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncStructureSearch {
    private static final int TIMEOUT_SECONDS = 30;
    private static final AtomicBoolean ACTIVE = new AtomicBoolean(false);
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Structure Search Thread");
        thread.setDaemon(true);
        return thread;
    });
    private static final ScheduledExecutorService TIMEOUT_EXECUTOR = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Structure Search Timeout Thread");
        thread.setDaemon(true);
        return thread;
    });

    public static <T> CompletableFuture<Result<T>> submit(Callable<T> callable) {
        if (!ACTIVE.compareAndSet(false, true)) {
            return CompletableFuture.completedFuture(Result.busy());
        }

        CompletableFuture<Result<T>> result = new CompletableFuture<>();
        Future<?> future = EXECUTOR.submit(() -> {
            try {
                result.complete(Result.success(callable.call()));
            } catch (Throwable throwable) {
                result.complete(Result.failed());
            } finally {
                ACTIVE.set(false);
            }
        });

        TIMEOUT_EXECUTOR.schedule(() -> {
            if (result.complete(Result.timeout())) {
                future.cancel(true);
            }
        }, TIMEOUT_SECONDS, TimeUnit.SECONDS);

        return result;
    }

    public record Result<T>(Status status, T value) {
        public static <T> Result<T> success(T value) {
            return new Result<>(Status.SUCCESS, value);
        }

        public static <T> Result<T> busy() {
            return new Result<>(Status.BUSY, null);
        }

        public static <T> Result<T> timeout() {
            return new Result<>(Status.TIMEOUT, null);
        }

        public static <T> Result<T> failed() {
            return new Result<>(Status.FAILED, null);
        }
    }

    public enum Status {
        SUCCESS,
        BUSY,
        TIMEOUT,
        FAILED
    }
}
