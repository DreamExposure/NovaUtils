package org.dreamexposure.novautils.events.network.crosstalk;

import org.dreamexposure.novautils.event.Event;
import org.json.JSONObject;

public class CrossTalkReceiveEvent extends Event {
    private final JSONObject data;
    private final String clientIp;
    private final int clientPort;
    private final int clientIndex;

    public CrossTalkReceiveEvent(JSONObject _data, String _clientIp, int _clientPort, int _clientIndex) {
        data = _data;
        clientIp = _clientIp;
        clientPort = _clientPort;
        clientIndex = _clientIndex;
    }

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
}