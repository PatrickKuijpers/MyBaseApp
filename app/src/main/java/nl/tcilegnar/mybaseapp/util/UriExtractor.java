package nl.tcilegnar.mybaseapp.util;

public class UriExtractor {
    public static String getParameter(String uri, String key) {
        String keyString = key + "=";
        String fromStartOfParamToEnd = uri.substring(uri.indexOf(keyString) + keyString.length());
        int indexOfNextParam = fromStartOfParamToEnd.indexOf("&");
        int lastIndexOfParam = indexOfNextParam > 0 ? indexOfNextParam : fromStartOfParamToEnd.length();
        return fromStartOfParamToEnd.substring(0, lastIndexOfParam);
    }
}
