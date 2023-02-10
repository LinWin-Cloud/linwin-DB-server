import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Data.Json;
import MirrorRuntime.Engine;
import javafx.scene.layout.ConstraintsBase;
import remote.Loader;
import remote.UserRemote;

public class MainApp {
    public static int boot = 0;
    public static String logPath;
    public static int update = 3600000;

    public static void main(String[] args) {
        /**
         * Load config file.
         */
        try {
            File CONFIG_FILE = new File("../../config/service/Distributed_Service.json");
            if (!CONFIG_FILE.exists() || !CONFIG_FILE.isFile()) {
                System.out.println("CAN NOT FIND CONFIG FILE: Distributed_Service.json");
                Runtime.getRuntime().exit(0);
            }
            String ServerPort = Json.readJson(CONFIG_FILE.getAbsolutePath(),"Service-Port");
            String LogPath = Json.readJson(CONFIG_FILE.getAbsolutePath(),"Log-Path");
            String update = Json.readJson(CONFIG_FILE.getAbsolutePath(),"Update-Log");
            String key = Json.readJson(CONFIG_FILE.getAbsolutePath(),"Key");

            MainApp.logPath = LogPath;
            MainApp.update = Integer.valueOf(update);
            UserRemote.MirrorKey_md5 = UserRemote.md5(key);

            if (!new File(logPath).exists() && !new File(logPath).isDirectory()) {
                System.out.println("CONFIG ERROR: CAN NOT FIND TARGET LOG PATH");
                System.exit(0);
            }
            Thread LOAD_REMOTE = new Thread(new Runnable() {
                @Override
                public void run() {
                    Loader loader = new Loader();
                    loader.loadUser();
                }
            });
            LOAD_REMOTE.start();
            Thread HTTP_THREAD = new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerSocket serverSocket = MainApp.getServerSocket(Integer.valueOf(ServerPort));
                    MainApp.Distributed_SERVICE(serverSocket);
                }
            });
            HTTP_THREAD.start();

        } catch (Exception exception){
            exception.printStackTrace();
            System.exit(0);
        }
    }
    public static ServerSocket getServerSocket(int port) {
        try{
            boot = boot + 1;
            System.out.println("Bind ServerSocket: localhost:"+port+"  |"+MainApp.boot+"|");
            return new ServerSocket(port);
        }catch (Exception exception){
            return MainApp.getServerSocket(port);
        }
    }
    public static void Distributed_SERVICE(ServerSocket serverSocket)
    {

        ExecutorService executorService = Executors.newFixedThreadPool(500);

        while (true) {
            try{
                Socket socket = serverSocket.accept();
                Callable<Integer> callable = new Callable<Integer>()
                {
                    @Override
                    public Integer call() throws Exception
                    {
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String getRequests = bufferedReader.readLine();

                        PrintWriter printWriter = new PrintWriter(outputStream);
                        getRequests = java.net.URLDecoder.decode(getRequests,"UTF-8");
                        getRequests = getRequests.substring(getRequests.indexOf(" ")+1, getRequests.lastIndexOf("HTTP/") -1 );

                        String keyWord_2 = "?Key=";
                        String keyWord_3 = "?Command=";

                        int s_2 = getRequests.lastIndexOf(keyWord_2);
                        int s_3 = getRequests.lastIndexOf(keyWord_3);

                        if (s_2 == -1 || s_3 == -1)
                        {
                            printWriter.println("HTTP/1.1 400 OK");
                            MainApp.sendTitle(printWriter);
                            printWriter.println("Send Message Error");
                            printWriter.flush();
                            socket.close();
                            return 1;
                        }
                        try
                        {
                            String getKey = getRequests.substring(s_2+keyWord_2.length(),s_3);
                            String getCommand = getRequests.substring(s_3+keyWord_3.length());
                            printWriter.println("HTTP/1.1 200 OK");
                            MainApp.sendTitle(printWriter);
                            if (UserRemote.MirrorKey_md5.equals(getKey)) {
                                Engine engine  = new Engine();
                                engine.exec(getCommand);
                                printWriter.println(engine.getMessage());
                                printWriter.flush();
                                socket.close();
                            }else {
                                printWriter.println("Key Error");
                                printWriter.flush();
                                socket.close();
                            }
                            return 0;
                        }
                        catch (Exception exception)
                        {
                            printWriter.println("HTTP/1.1 400 OK");
                            MainApp.sendTitle(printWriter);
                            printWriter.println("Send Message Error");
                            printWriter.flush();
                            socket.close();
                            return 1;
                        }
                    }
                };
                executorService.submit(callable);
            }
            catch (Exception exception){}
        }
    }
    public static void sendTitle(PrintWriter printWriter)
    {
        printWriter.println("Access-Control-Allow-Headers: *");
        printWriter.println("Access-Control-Allow-Origin: *");
        printWriter.println("Content-Type: text/plain");
        printWriter.println();
    }
}
