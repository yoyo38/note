package cn.holdmoral.forever.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/1/20 14:27
 */
public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public MaxSizeHashMap(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
