import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteSocket {
    public static void main(String[] args) {
        //Utilizado para leitura do teclado
        Scanner entrada = new Scanner(System.in);

        //variaável para armazenar o tezxto que será digitado
        String texto;

        Socket cliente;

        PrintStream saida;

        try {
            //cria o socket com os parametros
            cliente = new Socket("localhost", 7000);

            saida = new PrintStream(cliente.getOutputStream());


            ClientReceiver clientReceiver = ClientReceiver.getInstance(cliente);

            new Thread(clientReceiver).start();
            do {
                texto = entrada.nextLine();
                saida.println(texto);
            } while (!"sair".equals(texto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
