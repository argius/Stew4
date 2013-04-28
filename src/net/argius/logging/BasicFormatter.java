package net.argius.logging;

import java.io.*;
import java.text.*;
import java.util.logging.*;

/**
 * LoggingFormatter.
 */
public class BasicFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        final String msg = record.getMessage();
        final String formatted;
        Object[] args = record.getParameters();
        if (args != null && args.length > 0) {
            formatted = MessageFormat.format(msg, args);
        } else {
            formatted = msg;
        }
        final String s;
        final Throwable th = record.getThrown();
        if (th != null) {
            Writer w = new StringWriter();
            PrintWriter out = new PrintWriter(w);
            th.printStackTrace(out);
            s = w.toString();
        } else {
            s = "";
        }
        final String datetime = String.format("%1$tF %1$tT.%1$tL", record.getMillis());
        return String.format("%s %s %s#%s [%s] %s%n%s",
                             datetime,
                             Thread.currentThread().getName(),
                             record.getSourceClassName(),
                             record.getSourceMethodName(),
                             getLevelName(record.getLevel()),
                             formatted,
                             s);
    }

    private static String getLevelName(Level lv) {
        if (lv.equals(Level.FINE)) {
            return "DEBUG";
        }
        if (lv.equals(Level.FINER)) {
            return "TRACE";
        }
        return lv.toString();
    }

}
