package LinwinVOS.Mirror;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MirrorHost {
    private String name;
    private String key;
    private String remote;
    private String md5Key;

    public void setName(String name) {
        this.name = name;
    }
    public void setRemote(String remote) {
        this.remote = remote;
    }
    public String getRemote() {
        return this.remote;
    }
    public void setKey(String key) {
        this.key = key;
        this.md5Key = MirrorHost.md5(key);
    }
    public String getMd5Key() {
        return this.md5Key;
    }
    public String getKey() {
        return this.key;
    }
    public String getName() {
        return this.name;
    }
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No This Md5 Function");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    public String sendComamnd() {

    }
}
