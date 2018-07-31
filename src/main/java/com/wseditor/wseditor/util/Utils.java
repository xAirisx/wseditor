package com.wseditor.wseditor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {


    public static void trySilently(Action action) {
        try {
            action.call();
        } catch (Exception e) {

            throw new IllegalArgumentException(e);
        }
    }

    public interface Action {
        void call() throws Exception;
    }

}
