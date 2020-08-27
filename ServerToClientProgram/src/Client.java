
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException{

        // Create connections to server
        Socket socket =
                new Socket("localhost", 4999);
        Socket socket2 =
                new Socket("localhost", 4998);
        // Create print writer output stream to give to server
        PrintWriter pr =
                new PrintWriter(socket.getOutputStream());
        PrintWriter pr2 =
                new PrintWriter(socket2.getOutputStream());

        // Create a buffered reader for reading the file name and information
        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        )
                );

        // Get the file names and information
        String fileName = br.readLine();

        String information = br.readLine();

        // Put in the file Name and the file information
        pr.write(fileName);
        pr.close();

        pr2.write(information);
        pr2.close();

        // Read an error messages
        InputStreamReader errorInfoIn =
                new InputStreamReader(socket.getInputStream());
        BufferedReader readErrorInfo =
                    new BufferedReader(errorInfoIn);

        String errorInfo =
                readErrorInfo.readLine();
        System.out.println(errorInfo);
        errorInfoIn.close();

    }
}


