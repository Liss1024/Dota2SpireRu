package dota2Spire.util;

public class StringUtil {
    public static String format(String format, Object... args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                format = format.replace("{" + i + "}", args[i].toString());
            }
        }
        return format;
    }
}
