package com.kucuk.dw.client.callers.messageServiceCaller;

import com.kucuk.dw.client.callers.CallResult;

import java.util.concurrent.Callable;

public abstract class MessageServiceCallerBase implements Callable<CallResult> {
    @Override
    public abstract CallResult call() throws Exception;
}
