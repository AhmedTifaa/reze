package com.example.ahmed.reze1;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by tifaa on 05/05/18.
 */


public class SocketConnect {
    public static Socket socket;
    {
        try{
            socket = IO.socket("http://192.168.1.5:3000");

        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    public  SocketConnect(){
        socket.connect();
    }
}
