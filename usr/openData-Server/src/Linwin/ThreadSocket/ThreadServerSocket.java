package ThreadSocket;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class ThreadServerSocket {
    private int id;
    public int createID() {
        Random random = new Random();
        int id = random.nextInt(10000);
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
