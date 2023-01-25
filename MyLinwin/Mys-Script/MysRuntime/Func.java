package MysRuntime;


import java.util.Date;
import java.text.SimpleDateFormat;

public class Func {
    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        java.util.Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public static String getLastName(String str) {
        try{
            return str.substring(str.lastIndexOf("."),str.length());
        }catch (Exception exception){
            return "";
        }
    }
}
