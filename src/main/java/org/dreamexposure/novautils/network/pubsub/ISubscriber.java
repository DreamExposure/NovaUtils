package org.dreamexposure.novautils.network.pubsub;

import org.dreamexposure.novautils.database.RedisInfo;

import java.util.UUID;

/**
 * @author NovaFox161
 * Date Created: 11/26/2018
 * For Project: NovaUtils
 * Author Website: https://www.novamaday.com
 * Company Website: https://www.dreamexposure.org
 * Contact: nova@dreamexposure.org
 */
public interface ISubscriber {
    
    void subscribe();
    
    void unsubscribe();
    
    //Getters
    RedisInfo getInfo();
    
    int getClient();
    
    String getChannelName();
    
    UUID getUniqueId();
    
    //Setters
}