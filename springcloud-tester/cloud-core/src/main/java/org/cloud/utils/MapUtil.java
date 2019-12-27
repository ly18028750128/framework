package org.cloud.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public final class MapUtil {

    private MapUtil(){

    }

    private final static MapUtil instance = new MapUtil();

    public static MapUtil single(){
        return instance;
    }

    public Map<String,String> toStringMap(Map<String,?> mapValue){
        Map<String,String> result=new HashMap<>();
        for(String key:mapValue.keySet()){
            Object value = mapValue.get(key);
            if(value!=null){
                result.put(key, JSON.toJSONString(value));
            }
        }
        return result;
    }

}
