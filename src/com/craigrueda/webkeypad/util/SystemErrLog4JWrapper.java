package com.craigrueda.webkeypad.util;

import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class SystemErrLog4JWrapper implements InitializingBean {
	private static final Log log = LogFactory.getLog(SystemErrLog4JWrapper.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
        System.setErr(createLoggingProxy(System.err));
        
        System.err.println("SystemErrLog4JWrapper initialized...");
	}
	
	public static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String msg) {
                if (log.isErrorEnabled())
                	log.error(msg);
            }
        };
    }
}
