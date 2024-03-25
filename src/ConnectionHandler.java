import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {
    private long id;
    private Socket clientSocket;
    private List<ConnectionHandler> clientes;

    public void run() {
        BufferedReader in;
        id = Thread.currentThread().getId();
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream printStream = new PrintStream(clientSocket.getOutputStream());


            do {
                String texto = null;
                try {
                    texto = in.readLine();
                } catch (IOException e) {
                    clientes.remove(clientSocket);
                    clientSocket.close();
                }

                if (texto == null) {
                    break;
                }

                distributeMessage(texto, id);
                System.out.println(texto);
                /*printStream.println(texto);*/
            } while (!"sair".equals(in.toString()));
        } catch (IOException e) {

            try {
                clientes.remove(clientSocket);
                clientSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void distributeMessage(String message, long id) throws IOException {
        Object lock = new Object();
        List<ConnectionHandler> clientsCopy;
        synchronized (lock) {
            clientsCopy = new ArrayList<>(clientes);
        }
        for (ConnectionHandler client : clientsCopy) {
            if (client.getId() != this.getId()) {
                client.sendMessage(message);
            }
        }
    }

    private void sendMessage(String mensagem) throws IOException {
        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(mensagem);
        } catch (IOException e) {
            clientes.remove(clientSocket);
            clientSocket.close();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public List<ConnectionHandler> getClientes() {
        return clientes;
    }

    public void setClientes(List<ConnectionHandler> clientes) {
        this.clientes = clientes;
    }

    public ConnectionHandler(Socket conexao) {
        clientSocket = conexao;
    }
}
