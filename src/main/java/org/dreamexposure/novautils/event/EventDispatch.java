package org.dreamexposure.novautils.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EventDispatch {
    private static EventDispatch instance;

    private HashMap<Event<? extends Event>, List<IEventListener>> subs = new HashMap<>();

    private EventDispatch() {}

    public static EventDispatch get() {
        if (instance == null)
            instance = new EventDispatch();

        return instance;
    }

    public void subscribe(Event<? extends Event> event, IEventListener listener) {
        if (subs.containsKey(event)) {
            subs.get(event).add(listener);
        } else {
            subs.put(event, Collections.singletonList(listener));
        }
    }

    public void dispatch(Event<? extends Event> event) {
        if (subs.containsKey(event)){
            for (IEventListener el : subs.get(event)) {
                el.dispatch(event);
            }
        }
    }
}