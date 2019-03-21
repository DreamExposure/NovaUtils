package org.dreamexposure.novautils.network.crosstalk;

import org.dreamexposure.novautils.event.EventManager;
import org.dreamexposure.novautils.events.network.crosstalk.CrossTalkReceiveEvent;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Deprecated
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

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendToSpecificClient(JSONObject data, String sendToIp, int sendToPort) {
        try {
            //Add the additional data we need so that the CrossTalk server knows where this is to go.
            JSONObject input = new JSONObject();

            input.put("Client-IP", sendToIp);
            input.put("Client-Port", sendToPort);
            input.put("Client-Index", -1);
            input.put("Data", data);
            try {
                //Init socket
                Socket sock = new Socket(sendToIp, sendToPort);

                //Send data to CrossTalk Client
                DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
                ds.writeUTF(input.toString());
                ds.close();
                sock.close();
            } catch (Exception e) {
                //Failed to send to specific client....
                e.printStackTrace();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONObject sendAndReceive(JSONObject data) throws IOException {
        ServerSocket oneTimeServer = new ServerSocket(0);

        //Add the additional data
        JSONObject input = new JSONObject();

        input.put("Client-IP", "SERVER");
        input.put("Client-Port", 0);
        input.put("Client-Index", -1);
        input.put("One-Time-Response-Port", oneTimeServer.getLocalPort());
        input.put("Data", data);

        //Send to all clients... the correct client will respond.
        for (ClientSocketData csd : clients) {
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

        //Open server and wait for response....
        Socket client = oneTimeServer.accept();

        DataInputStream dis = new DataInputStream(client.getInputStream());
        String dataRaw = dis.readUTF();

        JSONObject dataOr = new JSONObject(dataRaw);

        //Parse
        JSONObject responseData = dataOr.getJSONObject("Data");

        dis.close();
        client.close();
        oneTimeServer.close();

        return responseData;
    }

    public static JSONObject sendAndReceive(JSONObject data, String sendToIp, int sendToPort) throws IOException {
        ServerSocket oneTimeServer = new ServerSocket(0);

        //Add the additional data
        JSONObject input = new JSONObject();

        input.put("Client-IP", "SERVER");
        input.put("Client-Port", 0);
        input.put("Client-Index", -1);
        input.put("One-Time-Response-Port", oneTimeServer.getLocalPort());
        input.put("Data", data);

        //Send to specific client
        try {
            //Init socket
            Socket sock = new Socket(sendToIp, sendToPort);

            //Send data to CrossTalk Client
            DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
            ds.writeUTF(input.toString());
            ds.close();
            sock.close();
        } catch (Exception e) {
            //Failed to send to specific client....
            e.printStackTrace();
        }


        //Open server and wait for response....
        Socket client = oneTimeServer.accept();

        DataInputStream dis = new DataInputStream(client.getInputStream());
        String dataRaw = dis.readUTF();

        JSONObject dataOr = new JSONObject(dataRaw);

        //Parse
        JSONObject responseData = dataOr.getJSONObject("Data");

        dis.close();
        client.close();
        oneTimeServer.close();

        return responseData;
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
                    JSONObject data = dataOr.getJSONObject("Data");
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

                    //Handle event -- NOTE: Event will fire even if Keep Alive in case server needs to handle it.
                    CrossTalkReceiveEvent event = new CrossTalkReceiveEvent(data, clientIp, clientPort, clientIndex);
                    if (dataOr.has("One-Time-Response-Port")) {
                        event.setRequireResponse(true);
                        event.setOneTimeResponsePort(dataOr.getInt("One-Time-Response-Port"));
                    }
                    EventManager.get().getEventBus().post(event);

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

    public static List<ClientSocketData> getClients() {
        return clients;
    }

}