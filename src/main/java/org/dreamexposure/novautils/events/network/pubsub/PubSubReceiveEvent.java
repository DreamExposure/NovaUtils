package org.dreamexposure.novautils.events.network.pubsub;

import org.json.JSONObject;

/**
 * @author NovaFox161
 * Date Created: 11/26/2018
 * For Project: NovaUtils
 * Author Website: https://www.novamaday.com
 * Company Website: https://www.dreamexposure.org
 * Contact: nova@dreamexposure.org
 */
@SuppressWarnings("unused")
public class PubSubReceiveEvent {
    
    private final JSONObject data;
    private final String channelName;
    private final int client;
    
    public PubSubReceiveEvent(JSONObject _data, int _client, String _channelName) {
        data = _data;
        client = _client;
        channelName = _channelName;
    }
    
    /**
     * Gets the original data the client sent.
     *
     * @return The original data the client sent.
     */
    public JSONObject getData() {
        return data;
    }
    
    public int getClient() {
        return client;
    }
    
    /**
     * Gets the channel of the client server the data was sent from.
     *
     * @return The channel of the client server the data was sent from.
     */
    public String getChannelName() {
        return channelName;
    }
}