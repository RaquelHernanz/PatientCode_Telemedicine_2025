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
    private static void connectToServer() throws IOException {
        if (socket == null || socket.isClosed()) { // si ya hay una conexión activa no se crea otro socket
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 9000); // para crear el socket, y conectarse al server
            printWriter = new PrintWriter(socket.getOutputStream(), true); // cada linea se envía inmediatamnte sin esperar a llenar el buffer
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

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
}


