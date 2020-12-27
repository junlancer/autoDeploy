package junlancer.utils;

public class StringUtil {
    public static boolean checkNull(String s) {
        return s == null || "".equals(s);
    }

    public static boolean checkNull(String... s) {
        for (String value : s) {
            if (checkNull(value)) {
                return true;
            }
        }
        return false;
    }
}
