package org.dreamexposure.novautils.network.crosstalk;

@Deprecated
public class ClientSocketData {
    private final String ip;
    private final int port;

    public ClientSocketData(String _ip, int _port) {
        ip = _ip;
        port = _port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}