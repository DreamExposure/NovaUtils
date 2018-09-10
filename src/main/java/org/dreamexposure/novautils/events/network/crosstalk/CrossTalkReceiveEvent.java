package org.dreamexposure.novautils.events.network.crosstalk;

import org.json.JSONObject;

public class CrossTalkReceiveEvent {
    private final JSONObject data;
    private final String clientIp;
    private final int clientPort;
    private final int clientIndex;

    private boolean requireResponse;
    private int oneTimeResponsePort;

    public CrossTalkReceiveEvent(JSONObject _data, String _clientIp, int _clientPort, int _clientIndex) {
        data = _data;
        clientIp = _clientIp;
        clientPort = _clientPort;
        clientIndex = _clientIndex;
    }

    //Getters
    public JSONObject getData() {
        return data;
    }

    public String getClientIp() {
        return clientIp;
    }

    public int getClientPort() {
        return clientPort;
    }

    public int getClientIndex() {
        return clientIndex;
    }

    public boolean isRequireResponse() {
        return requireResponse;
    }

    public int getOneTimeResponsePort() {
        return oneTimeResponsePort;
    }

    //Settings
    public void setRequireResponse(boolean _require) {
        requireResponse = _require;
    }

    public void setOneTimeResponsePort(int _port) {
        oneTimeResponsePort = _port;
    }
}