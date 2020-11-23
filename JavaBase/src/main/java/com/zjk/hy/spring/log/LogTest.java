/*
package com.zjk.hy.spring.log;

*/
/*import org.apache.commons.logging.LogFactory;
import org.apache.juli.logging.Log;*//*


*/
/*import java.util.logging.Logger;*//*


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
    public static void main(String[] args) {
        */
/*Log log = LogFactory.getLog("log4j");
        log.info("aaa");*//*

        Log log = LogFactory.getLog(LogTest.class);
        log.info("卧槽");

        */
/*Logger logger = Logger.getLogger("Foo");
        logger.info("Java Log");*//*


        Logger logger = LoggerFactory.getLogger(LogTest.class);
        logger.info("Hello World");

        java.util.logging.Logger aa = java.util.logging.Logger.getLogger("JUL");
        aa.info("java util log");
    }
}
*/
