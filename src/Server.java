import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private ServerSocket servidor;
    private Socket conexao = null;
    private List<ConnectionHandler> clientes = new ArrayList<>();
    private static Server server;


    @Override
    public void run() {

        try {

            while (true) {
                conexao = servidor.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(conexao);
                Object lock = new Object();
                synchronized (lock) {
                    clientes.add(connectionHandler);
                }
                for (ConnectionHandler cliente : clientes) {
                    cliente.setClientes(clientes);
                }
                new Thread(connectionHandler).start();
            }

        } catch (IOException e) {
            System.out.println("Algo de errado aconteceu");
        } finally {
            //encerro o socket de comunicação
            try {
                conexao.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                servidor.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Server(int porta) throws IOException {
        this.servidor = new ServerSocket(porta);
    }

    public static synchronized Server getInstance(int porta) throws IOException {
        if (server == null)
            server = new Server(porta);

        return server;
    }

}
