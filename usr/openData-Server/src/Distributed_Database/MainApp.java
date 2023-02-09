import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Data.Json;
import LinwinVOS.outPut.OutPutFileSystem;
import sun.awt.X11.XSystemTrayPeer;

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

            MainApp.logPath = LogPath;
            MainApp.update = Integer.valueOf(update);

            if (!new File(logPath).exists() && !new File(logPath).isDirectory()) {
                System.out.println("CONFIG ERROR: CAN NOT FIND TARGET LOG PATH");
                System.exit(0);
            }
            ServerSocket serverSocket = MainApp.getServerSocket(Integer.valueOf(ServerPort));
            MainApp.Distributed_SERVICE(serverSocket);

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
    public static void Distributed_SERVICE(ServerSocket serverSocket) {

        ExecutorService executorService = Executors.newFixedThreadPool(500);
        Socket socket = null;
        Socket finalSocket = socket;
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                InputStream inputStream = finalSocket.getInputStream();
                OutputStream outputStream = finalSocket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String getRequests = bufferedReader.readLine();

                PrintWriter printWriter = new PrintWriter(outputStream);
                getRequests = java.net.URLDecoder.decode(getRequests,"UTF-8");

                String requests
                return 0;
            }
        };
        while (true) {
            try{
                socket = serverSocket.accept();
                executorService.submit(callable);
            }
            catch (Exception exception){}
        }
    }
}
