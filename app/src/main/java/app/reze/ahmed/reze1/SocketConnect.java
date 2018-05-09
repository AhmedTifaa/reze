package app.reze.ahmed.reze1;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by tifaa on 05/05/18.
 */


public class SocketConnect {

    public static Socket socket;
    {

        try {
            socket = IO.socket("http://192.168.1.27:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public  SocketConnect(){
        socket.connect();
    }
}
