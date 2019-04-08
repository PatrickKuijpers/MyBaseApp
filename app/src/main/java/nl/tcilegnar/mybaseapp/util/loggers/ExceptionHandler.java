package nl.tcilegnar.mybaseapp.util.loggers;

import java.io.PrintWriter;
import java.io.StringWriter;

import nl.tcilegnar.mybaseapp.util.Finally;

public class ExceptionHandler {
    private static final String DEFAULT_TAG = ExceptionHandler.class.getSimpleName();

    private final String logTag;

    public ExceptionHandler() {
        this(DEFAULT_TAG);
    }

    /** TODO (PK): could be improved by injecting {@link Log} (if it wouldn't be static) to improve unittestability */
    public ExceptionHandler(String logTag) {
        this.logTag = logTag;
    }

    /**
     * Creates a new Exception to log and report to Firebase as non-fatal
     *
     * @param message the message for the new Exception
     */
    public void logAndReport(String message) {
        logAndReport(new Exception(message));
    }

    /**
     * Creates a new Exception to log and report to Firebase as non-fatal
     *
     * @param message   the message for the new Exception
     * @param throwable the cause of the new Exception to add as additional info
     */
    public void logAndReport(String message, Throwable throwable) {
        logAndReport(new Exception(message, throwable));
    }

    /** @param throwable the Throwable to log and report in Firebase as non-fatal */
    public void logAndReport(Throwable throwable) {
        printStackTrace(throwable);
        // TODO:       FirebaseCrashlyticsWrapper.logNonFatalException(throwable);
    }

    /** @param throwable the Throwable to log */
    public void printStackTrace(Throwable throwable, Log.Cat... categories) {
        if (Log.shouldLog()) {
            Log.w(logTag, throwable.getClass().getSimpleName() + ": " + throwable.getMessage() + " ... See " +
                    "stacktrace" + " below:", categories);
            throwable.printStackTrace();
        }
    }

    public static String getStacktraceString(Throwable throwable) {
        String stackTraceString = "";
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            stackTraceString = sw.toString();
        }
        catch (Exception ignored) {
        }
        finally {
            Finally.close(pw);
            Finally.close(sw);
        }
        return stackTraceString;
    }
}
