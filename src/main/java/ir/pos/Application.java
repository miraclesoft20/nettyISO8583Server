package ir.pos;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException, InterruptedException {
        POSServer posServer = new POSServer(9093);
        posServer.start();
    }
}
