package ThreadSocket;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadSocket {
    private static String[] Message = {""};
    public String getMessage() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String message = "";
                while (true) {
                    try{
                        String getMess = ThreadSocket.Message[0];
                        Thread.sleep(20);
                        String newMess = ThreadSocket.Message[0];
                        if (!getMess.equals(newMess))
                        {
                            message = newMess;
                            break;
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                return message;
            }
        });
        String socket = null;
        try{
            socket = future.get();
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        executorService.shutdown();
        return socket;
    }
    public void sendMessage(String message) {
        ThreadSocket.Message[0] = message;
    }
}
