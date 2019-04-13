package nl.tcilegnar.mybaseapp.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UriExtractorTest {
    private static final String TEST_URI = "myapp://tcilegnar.com/#access_token=abcde&scope=viewing_activity_read" +
            "&state=fghij&token_type=bearer";

    @Test
    public void getParameter_firstParam() {
        String value = UriExtractor.getParameter(TEST_URI, "access_token");
        assertEquals("abcde", value);
    }

    @Test
    public void getParameter_middleParam() {
        String value = UriExtractor.getParameter(TEST_URI, "state");
        assertEquals("fghij", value);
    }

    @Test
    public void getParameter_lastParam() {
        String value = UriExtractor.getParameter(TEST_URI, "token_type");
        assertEquals("bearer", value);
    }
}
