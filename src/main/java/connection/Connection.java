package connection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Connection {

    private static Socket socket;
    private static PrintWriter printWriter; //para escribir
    private static BufferedReader bufferedReader; // para leer

    // establecer la conexión al servidor
    public static void connectToServer() throws IOException { //si el servidor no está encendido entonces se lanza IOException
        if (socket == null || socket.isClosed()) { // si ya hay una conexión activa no se crea otro socket
            System.out.println("Connecting to server...");
            socket = new Socket("10.60.118.196", 9000); // para crear el socket, y conectarse al server.
            // Habría que cambiar el localhost por el ip del server. LocalHost es mi propio ordenador
            printWriter = new PrintWriter(socket.getOutputStream(), true); // envía texto al servidor, cada linea se envía inmediatamnte sin esperar a llenar el buffer
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // lee el texto que le envía el servidor
        }
    }

    // comprobar si hay conexión con el server
    public static boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    // en el resto de clases poner este métdo para comprobar si está conectdo el patient al server
    // si no lo está, se llama a la primera clase de ConnectToServer y se conecta

    // if (!Connection.isConnected()) {
    //    Connection.connectToServer();


    // cerrar la conexión con el servidor
    public static void releaseResources() {
        // Cerrar PrintWriter (no lanza IOException)
        if (printWriter != null) {
            printWriter.close();
        }
        // Cerrar BufferedReader y Socket
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void sendMessage(String message) { //para enviar mensaje al servidor
        if (printWriter != null) { // comprobar que la conexión con el servidor está activa.
            // Si printWriter == null, entonces no existe la conexión porque el socket no está activo y entonces no se ha creado el printWriter
            printWriter.println(message); // se envía el mensaje al otro lado del socket, al server
            System.out.println("[Patient] Enviado: " + message); //imprime esto por pantalla para que el patient lo sepa
        }
    }

    public static String readMessage() throws IOException { // lanza excepción porque depende de datos externos
        // si se apaga el servidor, se pierde conexión, etc entonces se lanza error
        if (bufferedReader != null) { // comprobar que la conexión con el servidor está activa.
            // Si printWriter == null, entonces no existe la conexión porque el socket no está activo y entonces no se ha creado el printWriter
            String response = bufferedReader.readLine();  // lee lo que le manda el server
            System.out.println("[Patient] Recibido: " + response); // imprime el mensaje del server por pantalla
            return response;
        }
        return null;
    }

    public static String sendAndRead(String message) throws IOException {
        // 1. Asegurar que estamos conectados
        if (!isConnected()) {
            connectToServer();
        }

        // 2. Enviar la petición al servidor
        sendMessage(message);

        // 3. Bloquear y esperar la respuesta
        return readMessage();
    }

}


