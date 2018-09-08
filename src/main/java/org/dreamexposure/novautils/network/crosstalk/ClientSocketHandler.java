package org.dreamexposure.novautils.network.crosstalk;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings({"unused", "Duplicates"})
public class ClientSocketHandler {
    private static ServerSocket serverSocket;
    private static Thread listenerTread;

    private static String serverHostname;
    private static int serverPort;

    private static String clientHostname;
    private static int clientPort;

    public static boolean sendToServer(int clientIndex, JSONObject data) {
        try {
            //Add the additional data we need so that the Bungee CrossTalk server knows where this is to go.
            JSONObject input = new JSONObject();
            Socket sock;

            input.put("Client-IP", clientHostname);
            input.put("Client-Port", clientPort);
            input.put("Client-Index", clientIndex);
            input.put("Data", data);

            //Init socket
            sock = new Socket(serverHostname, serverPort);

            //Send data to CrossTalk Server
            DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
            ds.writeUTF(input.toString());
            ds.close();
            sock.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setValues(String _serverHostname, int _serverPort, String _clientHostname, int _clientPort) {
        serverHostname = _serverHostname;
        serverPort = _serverPort;
        clientHostname = _clientHostname;
        clientPort = _clientPort;
    }

    public static void initListener() {
        try {
            serverSocket = new ServerSocket(clientPort);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        listenerTread = new Thread(() -> {
            while (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();

                    DataInputStream dis = new DataInputStream(client.getInputStream());
                    String dataRaw = dis.readUTF();

                    JSONObject dataOr = new JSONObject(dataRaw);

                    //Parse
                    JSONObject data = new JSONObject(dataOr.getJSONObject("Data"));
                    String clientIp = dataOr.getString("Client-IP");
                    String clientPlugin = dataOr.getString("Client-Index");

                    dis.close();
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listenerTread.setDaemon(true);
        listenerTread.start();
    }

    @SuppressWarnings("deprecation")
    public static void shutdownListener() {
        listenerTread.stop();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
