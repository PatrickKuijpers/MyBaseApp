package nl.tcilegnar.mybaseapp.util;

import java.io.Closeable;

import nl.tcilegnar.mybaseapp.util.loggers.ExceptionHandler;

public class Finally {
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (Exception e) {
            new ExceptionHandler().logAndReport(e);
        }
    }
}
