package com.zjy.service.component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesHelper {
    private HashMap<String, String> properties = new HashMap<>();

    private PropertiesHelper() {
    }

    private static PropertiesHelper instance = new PropertiesHelper();

    static {
        PropertiesHelper.getInstance().init();
    }

    public static PropertiesHelper getInstance() {
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

                PropertiesHelper.getInstance().setProperties(key, value);
            }
        } catch (Exception e) {
        }
    }
}
