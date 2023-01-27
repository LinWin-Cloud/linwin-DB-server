package connect;
public class Base64 {
    public static String addPasswd(String str) {
        return java.util.Base64.getUrlEncoder().encodeToString(str.getBytes());
    }
    public static String DecodePasswd(String str) throws Exception {
        str = str.replace("\r","");
        str = str.replace("\n","");
        byte[] decodedBytes = java.util.Base64.getMimeDecoder().decode(str);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }
}
