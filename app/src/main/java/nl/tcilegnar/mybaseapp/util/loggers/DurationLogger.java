package nl.tcilegnar.mybaseapp.util.loggers;

public class DurationLogger {
    private static final String TAG = Log.getTag(DurationLogger.class, Log.Cat.TIMER);

    public static final int DEFAULT_START_TIME = 0;
    public static final int INVALID_TIME = -1;

    private final boolean shouldLogSelf;
    private long startTime = DEFAULT_START_TIME;

    public DurationLogger() {
        this(true);
    }

    public DurationLogger(boolean shouldLogSelf) {
        this.shouldLogSelf = shouldLogSelf;
    }

    /** @return startTime in millis */
    @SuppressWarnings("UnusedReturnValue")
    public long start() {
        return start(System.currentTimeMillis());
    }

    public long start(long startTime) {
        if (shouldLogSelf) {
            Log.v(TAG, "Start: " + hashCode());
        }
        this.startTime = startTime;
        return startTime;
    }

    /** @return totalTime (between start & end) in millis */
    public long end() {
        if (startTime == DEFAULT_START_TIME) {
            return INVALID_TIME;
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if (shouldLogSelf) {
            Log.v(TAG, "End: " + hashCode() + " - Total time: " + totalTime);
        }

        reset();

        return totalTime;
    }

    public void reset() {
        startTime = DEFAULT_START_TIME;
    }
}
