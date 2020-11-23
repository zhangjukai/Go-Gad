package com.zjk.hy.se.log;

import java.util.logging.Logger;

public class JulTest {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("com.zjk.hy.se.log");
        logger.severe("severe");
        logger.warning("warning");
        logger.info("info");
        logger.config("cofnig");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }
}
