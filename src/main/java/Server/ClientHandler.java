package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private static final String SERVER_DIRECTORY = "./server_files";
    private Socket socket;
    private List<ClientHandler> allClients;
    private String username;
    private InputStream inputStream
    private OutputStream outputStream;
    private BufferedReader reader;
    private PrintWriter writer;


    public ClientHandler(Socket clientSokect, ArrayList<ClientHandler> clients) {

            this.socket = socket;
            this.allClients = allClients;
            try{
                InputStream input = socket.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(input);
                reader = new BufferedReader(inputReader);

                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output , true);
            } catch (IOException e) {
                System.out.println("Error initializing client I/O streams: " + e.getMessage());
            }
    }

    @Override
    public void run() {
        try {
            String msg;

            while ((msg = reader.readLine()) != null) {
                // TODO: Read incoming message from the input stream
                System.out.println("Received:" + msg);
                // TODO: Process the message
                broadcast("["+socket.getInetAddress()+"]"+msg);
            }
        } catch (Exception e) {

        } finally {
            //TODO: Update the clients list in Server
            allClients.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void sendMessage(String msg){
        //TODO: send the message (chat) to the client
        writer.println(msg);
    }
    private void broadcast(String msg) throws IOException {
        //TODO: send the message to every other user currently in the chat room
        synchronized (allClients) {
            for (ClientHandler client : allClients){
                if (client != this){
                    client.sendMessage(msg);
                }
            }
        }
    }

    private void sendFileList(){
        // TODO: List all files in the server directory
        File dir = new File(SERVER_DIRECTORY);
        if (!dir.exists() || !dir.isDirectory()){
            writer.println("ERROR: Server directory not found.");
            return;
        }
        // TODO: Send a message containing file names as a comma-separated string
        File[] files = dir.listFiles();
        if (files == null || files.length == 0){
            writer.println("No files available on server.");
            return;
        }

        StringBuilder fileList = new StringBuilder();
        for (File file : files){
            if(file.isFile()){
                fileList.append(file.getName()).append(",");
            }
        }
        // remove the ","
        if(fileList.length() > 0){
            fileList.setLength((fileList.length() - 1));
        }
        writer.println("Files: " + fileList);
    }

    private void sendFile(String fileName) throws FileNotFoundException {
        File file = new File(SERVER_DIRECTORY);
        // TODO: Send file name and size to client
        if (!file.exists() || !file.isFile()){
            writer.println("ERROR: File not found");
            return;
        }
        // TODO: Send file content as raw bytes
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file))){
            writer.println("FILE_START:" + file.getName() + ":" + file.length());

            byte[] buffer = new byte[4096];
            int byteRead;
            while ((byteRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer,0 , byteRead);
                outputStream.flush();
            }
            writer.println("FILE_END");

        } catch (IOException e) {
            writer.println("ERROR: Failed to send file.");
        }
    }
    private void receiveFile(String filename, int fileLength) throws FileNotFoundException {
        File dir = new File(SERVER_DIRECTORY);

        if (!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir , filename);
        try (BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[4096];
            int remaining = fileLength;
            int byteRead;
            while (remaining > 0 && (byteRead = inputStream.read(buffer , 0 ,Math.min(buffer.length , remaining))) != -1 ){
                fileOut.write(buffer, 0 , byteRead);
                remaining -= byteRead;
            }
            fileOut.flush();
            writer.println("UPLOAD_SUCCESSFUL");
        } catch (IOException e) {
            writer.println("ERROR: Failed to receive file.");;
        }

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
