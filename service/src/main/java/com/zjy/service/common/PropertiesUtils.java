package com.zjy.service.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
    private HashMap<String, String> properties = new HashMap<>();

    private PropertiesUtils() {
    }

    private static PropertiesUtils instance = new PropertiesUtils();

    static {
        PropertiesUtils.getInstance().init();
    }

    public static PropertiesUtils getInstance() {
        return instance;
    }

    public void setProperties(String key, String value) {
        properties.put(key, value);
    }

    public String getProperties(String key) {
        return properties.get(key);
    }

    private void init() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("application-common.yml");
        Properties propertiesTemp = new Properties();
        try {
            propertiesTemp.load(is);
            for (Map.Entry<Object, Object> kp : propertiesTemp.entrySet()) {
                String key = (String) kp.getKey();
                String value = (String) kp.getValue();

                PropertiesUtils.getInstance().setProperties(key, value);
            }
        } catch (Exception e) {
        }
    }
}
