import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class Server {

    static String errorMsg = "No Errors, program has run successfully";

    public static void main(String[] args) throws IOException {

        // Get both connections to the client
        ServerSocket serverSocket =
                new ServerSocket(4999);
        ServerSocket serverSocket2 =
                new ServerSocket(4998);
        Socket socket =
                serverSocket.accept();
        Socket socket2 =
                serverSocket2.accept();

        // Confirm client connection
        System.out.println("The client has been connected to the server");

        // Get the streams from the client containing the string
        InputStreamReader fileNameIn =
                new InputStreamReader(socket.getInputStream());
        BufferedReader br =
                new BufferedReader(fileNameIn);

        InputStreamReader informationIn =
                new InputStreamReader(socket2.getInputStream());
        BufferedReader br2 =
                new BufferedReader(informationIn);

        // Read and assign the strings
        String fileName =
                br.readLine();
        String fileInformation =
                br2.readLine();

        // Get char array to write to the created file
        char[] fileInformationC =
                fileInformation.toCharArray();

        // Create file and write to it
        try (FileChannel fileChannel =
                     (FileChannel) Files.newByteChannel(
                             Path.of("C:\\Users\\Emman\\" + fileName + ".txt"),
                             StandardOpenOption.CREATE,
                             StandardOpenOption.WRITE
                     )) {

            int allocatedVal = 128;

            ByteBuffer byteBuffer =
                    ByteBuffer.allocate(allocatedVal);


            for (char c : fileInformationC){
                byteBuffer.put((byte) c);
                if(!byteBuffer.hasRemaining())
                    byteBuffer =
                            ByteBuffer.allocate(allocatedVal * 2);
            }

            byteBuffer.rewind();

            fileChannel.write(byteBuffer);



            System.out.println("File has been created and written to, \n" +
                    "Press Enter to open file");

            // The input does not matter
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            ProcessBuilder processBuilder =
                    new ProcessBuilder("notepad.exe", "C:\\Users\\Emman\\" + fileName + ".txt");
            processBuilder.start();

            PrintWriter printToClient =
                    new PrintWriter(socket.getOutputStream());

            printToClient.write(errorMsg);
        } catch (InvalidPathException e) {
            PrintWriter printToClient =
                    new PrintWriter(socket.getOutputStream());

            errorMsg = "Error in creating/writing to file \\n\" +\n" +
                    "Due to incorrect/invalid path: " + "\n" +
                    e;

            printToClient.write(errorMsg);
            printToClient.close();
        } catch (IOException e) {
            PrintWriter printToClient =
                    new PrintWriter(socket.getOutputStream());

            errorMsg = "Error in creating/writing to file \n" +
                    "Due to error in IO: " +
                    e;

            printToClient.write(errorMsg);
            printToClient.close();

        } catch (BufferOverflowException e) {

            PrintWriter printToClient =
                    new PrintWriter(socket.getOutputStream());

            errorMsg = "Error in creating/writing to file \n" +
            "Due to a buffer overflow: " +
                    e;

            printToClient.write(errorMsg);
            printToClient.close();
        }

    }
}
