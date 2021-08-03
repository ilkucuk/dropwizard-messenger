package com.kucuk.dw.client.caller;

import java.util.concurrent.Callable;

public interface MessageServiceCaller extends Callable<CallResult> {
    @Override
    CallResult call() throws Exception;
}
