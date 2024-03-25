import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver implements Runnable {
    private static ClientReceiver clientReceiver;
    private Socket clientSocket;

    @Override
    public void run() {
        while (true) {
            InputStream input;
            try {
                input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String time = reader.readLine();
                System.out.println(time);
            } catch (IOException e) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static ClientReceiver getInstance(Socket conexao) {
        if (clientReceiver == null) {
            clientReceiver = new ClientReceiver(conexao);
        }
        return clientReceiver;
    }

    private ClientReceiver(Socket conexao) {
        clientSocket = conexao;
    }
}
