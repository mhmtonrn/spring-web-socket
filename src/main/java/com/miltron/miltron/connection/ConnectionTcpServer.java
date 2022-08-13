package com.miltron.miltron.connection;

import com.miltron.miltron.utils.RawDataToTelemetryModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionTcpServer implements Runnable {
    private SimpMessagingTemplate client;
    private volatile int port;

    public ConnectionTcpServer(SimpMessagingTemplate client, int port) {
        this.port = port;
        this.client = client;
    }


    @Override
    public void run() {

        try {
            Socket socket = new Socket("localhost", port);
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[36];
            while ((in.read(buf)) != -1) {
                in.read(buf);
                System.out.println(RawDataToTelemetryModel.getModel(buf));
                client.convertAndSend("/topic/" + port, RawDataToTelemetryModel.getModel(buf));
            }
        } catch (IOException e) {
            System.out.println("Conneciton refused> " + e.getLocalizedMessage());
        }
    }
}
