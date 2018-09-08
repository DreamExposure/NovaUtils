package org.dreamexposure.novautils.network.crosstalk;

import org.dreamexposure.novautils.event.EventDispatch;
import org.dreamexposure.novautils.events.network.crosstalk.CrossTalkReceiveEvent;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "Duplicates"})
public class ServerSocketHandler {
    private static ServerSocket serverSocket;
    private static Thread listenerTread;

    private static int serverPort;

    private static final List<ClientSocketData> clients = new ArrayList<>();

    public static boolean sendToAllClients(JSONObject data, String clientIp, int clientPort, int clientIndex) {
        try {
            //Add the additional data we need so that the CrossTalk server knows where this is to go.
            JSONObject input = new JSONObject();

            input.put("Client-IP", clientIp);
            input.put("Client-Port", clientPort);
            input.put("Client-Index", clientIndex);
            input.put("Data", data);

            //Send to all clients...
            for (ClientSocketData csd : clients) {
                //Don't send back to the client that just sent the message....
                if (csd.getPort() != clientPort && !csd.getIp().equals(clientIp)) {
                    try {
                        //Init socket
                        Socket sock = new Socket(csd.getIp(), csd.getPort());

                        //Send data to CrossTalk Client
                        DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
                        ds.writeUTF(input.toString());
                        ds.close();
                        sock.close();
                    } catch (Exception e) {
                        //Failed to send to specific client....
                        e.printStackTrace();
                    }
                }
            }

           //Handle event
            CrossTalkReceiveEvent event = new CrossTalkReceiveEvent(data, clientIp, clientPort, clientIndex);
            EventDispatch.get().dispatch(event);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setValues(int _serverPort) {
        serverPort = _serverPort;
    }

    public static void initListener() {
        try {
            serverSocket = new ServerSocket(serverPort);
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
                    int clientPort = dataOr.getInt("Client-Port");
                    int clientIndex = dataOr.getInt("Client-Index");

                    if (data.has("Reason") && data.getString("Reason").equalsIgnoreCase("Keep-Alive")) {
                        //Keep alive...
                        if (!isInList(clientIp, clientPort)) {
                            ClientSocketData csd = new ClientSocketData(clientIp, clientPort);
                            clients.add(csd);
                        }
                    } else {
                        //Send to all clients!!!!
                        sendToAllClients(data, clientIp, clientPort, clientIndex);
                    }

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

    private static boolean isInList(String hostname, int port) {
        for (ClientSocketData cds : clients) {
            if (cds.getPort() == port && cds.getIp().equals(hostname))
                return true;
        }
        return false;
    }
}