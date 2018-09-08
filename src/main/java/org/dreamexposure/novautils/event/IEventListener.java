package org.dreamexposure.novautils.event;

public interface IEventListener {
    void dispatch(Event<? extends Event> event);
}