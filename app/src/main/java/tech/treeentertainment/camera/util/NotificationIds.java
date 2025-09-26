
package tech.treeentertainment.camera.util;

import java.util.HashMap;
import java.util.Map;

public class NotificationIds {

    private static NotificationIds instance = new NotificationIds();

    public static NotificationIds getInstance() {
        return instance;
    }

    private final Map<String, Integer> map = new HashMap<String, Integer>();
    private int counter;

    public int getUniqueIdentifier(String name) {
        Integer i = map.get(name);
        if (i != null) {
            return i.intValue();
        }
        ++counter;
        map.put(name, counter);
        return counter;
    }
}
