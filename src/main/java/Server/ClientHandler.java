package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    // TODO: Declare a variable to hold the input stream from the socket
    // TODO: Declare a variable to hold the output stream from the socket
    private List<ClientHandler> allClients;
    private String username;

    public ClientHandler(Socket clientSokect, ArrayList<ClientHandler> clients) {

            this.socket = socket;
            this.allClients = allClients;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // TODO: Read incoming message from the input stream
                // TODO: Process the message
            }
        } catch (Exception e) {

        } finally {
            //TODO: Update the clients list in Server
        }
    }


    private void sendMessage(String msg){
        //TODO: send the message (chat) to the client
    }
    private void broadcast(String msg) throws IOException {
        //TODO: send the message to every other user currently in the chat room
    }

    private void sendFileList(){
        // TODO: List all files in the server directory
        // TODO: Send a message containing file names as a comma-separated string
    }
    private void sendFile(String fileName){
        // TODO: Send file name and size to client
        // TODO: Send file content as raw bytes
    }
    private void receiveFile(String filename, int fileLength)
    {
        // TODO: Receive uploaded file content and store it in a byte array
        // TODO: after the upload is done, save it using saveUploadedFile
    }
    private void saveUploadedFile(String filename, byte[] data) throws IOException {
        // TODO: Save the byte array to a file in the Server's resources folder
    }

    private void handleLogin(String username, String password) throws IOException, ClassNotFoundException {
        // TODO: Call Server.authenticate(username, password) to check credentials
        // TODO: Send success or failure response to the client
    }

}
