package sqlDrive;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SqlDrive {
    private String userName;
    private String passwd;
    private String sendCommand;
    private String host;
    private String message;
    private int line;
    public SqlDrive(String host) {
        this.host = host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public Boolean sendCommand(String command) {
        try{
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(host+"/?Logon="+this.userName+"?Passwd="+Md5_tool.md5(this.passwd)+"?Command="+command).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer stringBuffer = new StringBuffer("");
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append("\n");
                stringBuffer.append(line);
                i = i + 1;
            }
            this.line = i - 1;
            if ((i - 1) == 0) {
                this.message =stringBuffer.toString();
                return false;
            }else {
                this.message = stringBuffer.toString();
                return true;
            }
        }catch (Exception exception){
            return false;
        }
    }
    public String getMessage() {
        return this.message;
    }
    public int getLine() {
        return this.line;
    }
}
