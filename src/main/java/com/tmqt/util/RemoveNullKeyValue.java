package com.tmqt.util;

/**
 * Created by chenw on 2018/12/19.
 */
import java.util.*;

public class RemoveNullKeyValue {
    /*移除Map中值为空的键值对*/
    public static void removeNullEntry(Map map) {
        removeNullKey(map);
        removeNullValue(map);
    }
    /*移除键为空的键值对*/
    public static void removeNullKey(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = (Object) iterator.next();
            remove(obj, iterator);
        }
    }
    /*移除值为空的键值对*/
    public static void removeNullValue(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = (Object) iterator.next();
            Object value = (Object) map.get(obj);
            remove(value, iterator);
        }
    }

    private static void remove(Object obj, Iterator iterator) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str == null || str.trim().isEmpty()) {
                iterator.remove();
            }
        } else if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            if (col == null || col.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Map) {
            Map temp = (Map) obj;
            if (temp == null || temp.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            if (array == null || array.length <= 0) {
                iterator.remove();
            }
        } else {
            if (obj == null) {
                iterator.remove();
            }
        }
    }
}
