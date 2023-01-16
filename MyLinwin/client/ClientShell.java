import java.io.*;
import java.net.Socket;

public class ClientShell {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("127.0.0.1",8888);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            String user = "root";
            String passwd = Md5Util.md5("123456");
            String command = "list database";
            String message = "Logon="+user+"?Passwd="+passwd+"?Command="+command;
            System.out.println(message);
            printWriter.println(message);
            printWriter.flush();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String getMessage = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();
            System.out.println(getMessage);
            socket.close();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
