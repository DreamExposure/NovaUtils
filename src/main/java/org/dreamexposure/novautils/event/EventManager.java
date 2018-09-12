package org.dreamexposure.novautils.event;

import com.google.common.eventbus.EventBus;

/**
 * @author NovaFox161
 * Date Created: 9/8/2018
 * For Project: NovaUtils
 * Author Website: https://www.novamaday.com
 * Company Website: https://www.dreamexposure.org
 * Contact: nova@dreamexposure.org
 */
public class EventManager {
    private static EventManager instance;

    private EventBus eventBus;

    private EventManager() {
    }

    public static EventManager get() {
        if (instance == null)
            instance = new EventManager();

        return instance;
    }

    public void init() {
        eventBus = new EventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

}
