import java.io.IOException;

public class ServidorSocket {
    public static void main(String[] args) throws IOException {
    Server server = Server.getInstance(7000);
    server.run();
    }
}
