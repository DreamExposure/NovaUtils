package org.dreamexposure.novautils.event;

import java.util.ArrayList;
import java.util.List;

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

    private List<EventListenerWrapper> listeners = new ArrayList<>();

    private EventManager() {
    }

    public static EventManager get() {
        if (instance == null)
            instance = new EventManager();

        return instance;
    }

    /**
     * Registers an event listener to have it's methods called when an event is fired
     *
     * @param listener The listener to register
     */
    public void registerEventListener(EventListener listener) {
        EventListenerWrapper wrapper = new EventListenerWrapper(listener);
        listeners.add(wrapper);
    }

    /**
     * Fires an event
     *
     * @param o The event to fire
     */
    public void fireEvent(Object o) {
        for (EventListenerWrapper wrapper : listeners) {
            wrapper.callEvent(o);
        }
    }
}
