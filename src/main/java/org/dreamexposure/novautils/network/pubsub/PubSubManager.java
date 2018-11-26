package org.dreamexposure.novautils.network.pubsub;

import org.dreamexposure.novautils.database.DatabaseManager;
import org.dreamexposure.novautils.database.DatabaseSettings;
import org.dreamexposure.novautils.database.RedisInfo;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author NovaFox161
 * Date Created: 11/26/2018
 * For Project: NovaUtils
 * Author Website: https://www.novamaday.com
 * Company Website: https://www.dreamexposure.org
 * Contact: nova@dreamexposure.org
 */
public class PubSubManager {
    private static PubSubManager instance;
    
    private RedisInfo info;
    
    private List<ISubscriber> subscribers = new ArrayList<>();
    
    private PubSubManager() {
    } //Prevent initialization
    
    public static PubSubManager get() {
        if (instance == null) instance = new PubSubManager();
        
        return instance;
    }
    
    //Getters
    public RedisInfo getInfo() {
        return info;
    }
    
    public ISubscriber getSubscriber(UUID id) {
        for (ISubscriber s : subscribers) {
            if (s.getUniqueId().equals(id)) return s;
        }
        return null;
    }
    
    //Setters
    
    //Functions
    public void init(String hostname, int port, String user, String pass) {
        DatabaseSettings settings = new DatabaseSettings(hostname, port + "", "N/a", user, pass, "N/a");
        info = DatabaseManager.connectToRedis(settings);
    }
    
    public boolean isRegistered(UUID id) {
        for (ISubscriber s : subscribers) {
            if (s.getUniqueId().equals(id)) return true;
        }
        return false;
    }
    
    public UUID register(int client, String channel) {
        BaseSubscriber subscriber = new BaseSubscriber(info, client, channel);
        
        subscribers.add(subscriber);
        
        subscriber.subscribe();
        
        return subscriber.getUniqueId();
    }
    
    public void unregister(UUID id) {
        if (isRegistered(id)) {
            ISubscriber sub = getSubscriber(id);
            sub.unsubscribe();
            
            subscribers.remove(sub);
        }
    }
    
    public void unregisterAll(int client) {
        List<ISubscriber> toRemove = new ArrayList<>();
        
        for (ISubscriber s : subscribers) {
            if (s.getClient() == client) {
                toRemove.add(s);
                s.unsubscribe();
            }
            
        }
        subscribers.removeAll(toRemove);
    }
    
    public void publish(String channel, int client, JSONObject data) {
        //Add server data...
        
        JSONObject toSend = new JSONObject();
        toSend.put("client", client);
        toSend.put("data", data);
        
        
        info.getClient().connect().async().publish(channel, toSend.toString());
    }
}
