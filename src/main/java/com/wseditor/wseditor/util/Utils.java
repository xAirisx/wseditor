package com.wseditor.wseditor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void trySilently(Action action) {
        try {
            action.call();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    public interface Action {
        void call() throws Exception;
    }

}
