package org.dreamexposure.novautils.network.pubsub;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.dreamexposure.novautils.database.RedisInfo;
import org.dreamexposure.novautils.event.EventManager;
import org.dreamexposure.novautils.events.network.pubsub.PubSubReceiveEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author NovaFox161
 * Date Created: 11/26/2018
 * For Project: NovaUtils
 * Author Website: https://www.novamaday.com
 * Company Website: https://www.dreamexposure.org
 * Contact: nova@dreamexposure.org
 */
public class BaseSubscriber implements ISubscriber {
    private final RedisInfo info;
    private final int client;
    private final String channelName;
    private final UUID uuid;
    
    private RedisPubSubAdapter<String, String> listener;
    private StatefulRedisPubSubConnection<String, String> connection;
    
    public BaseSubscriber(RedisInfo _info, int _client, String _channelName) {
        info = _info;
        client = _client;
        channelName = _channelName;
        uuid = UUID.randomUUID();
    }
    
    
    @Override
    public void subscribe() {
        connection = info.getClient().connectPubSub();
        
        listener = new RedisPubSubAdapter<String, String>() {
            @SuppressWarnings("Duplicates")
            @Override
            public void message(String channel, String message) {
                
                try {
                    JSONObject rawData = new JSONObject(message);
                    int client = rawData.getInt("client");
                    JSONObject data = rawData.getJSONObject("data");
                    
                    PubSubReceiveEvent event = new PubSubReceiveEvent(data, client, channel);
                    
                    EventManager.get().getEventBus().post(event);
                    
                } catch (JSONException e) {
                    System.out.println("PubSub Message received is not JSON!");
                }
            }
        };
        
        connection.addListener(listener);
        connection.sync().subscribe(channelName);
    }
    
    @Override
    public void unsubscribe() {
        connection.removeListener(listener);
        connection.sync().unsubscribe(channelName);
        
        connection.close();
    }
    
    @Override
    public RedisInfo getInfo() {
        return info;
    }
    
    @Override
    public int getClient() {
        return client;
    }
    
    @Override
    public String getChannelName() {
        return channelName;
    }
    
    @Override
    public UUID getUniqueId() {
        return uuid;
    }
}
