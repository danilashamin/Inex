package ru.messenger.inex_messenger.utils;

public class ReplacingSerialSingleThreadExecutor extends SerialSingleThreadExecutor {

    public ReplacingSerialSingleThreadExecutor(boolean prepareLooper) {
        super(prepareLooper);
    }

    @Override
    public synchronized void execute(final Runnable r) {
        tasks.clear();
        super.execute(r);
    }
}
