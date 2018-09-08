package org.dreamexposure.novautils.event;

import java.lang.reflect.Method;

/**
 * @author NovaFox161
 * Date Created: 9/8/2018
 * For Project: NovaUtils
 * Author Website: https://www.novamaday.com
 * Company Website: https://www.dreamexposure.org
 * Contact: nova@dreamexposure.org
 */
public class EventListenerWrapper {
    private EventListener listener;

    protected EventListenerWrapper(EventListener listener) {
        this.listener = listener;
    }

    protected void callEvent(Object event) {
        for (Method method : listener.getClass().getMethods()) {
            try {
                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length == 1) {
                    Class<?> parameter = parameterTypes[0];

                    if (parameter.getCanonicalName().equalsIgnoreCase(event.getClass().getCanonicalName())) {
                        method.setAccessible(true);
                        method.invoke(listener, event);
                    }

                }
            } catch (Exception e) {
                System.out.println("[ERROR] Failed to invoke listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
