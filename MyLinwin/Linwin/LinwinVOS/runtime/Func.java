package LinwinVOS.runtime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Func {
    public static String getNowTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss");
        return formatter.toString();
    }
}
