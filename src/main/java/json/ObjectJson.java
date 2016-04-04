/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import model.User;

/**
 *
 * @author carlos
 */
public class ObjectJson {
    
    public static <T>String getObjectJson(T u){
        Gson gson = new Gson();
        Type type = new TypeToken<T>() {}.getType();
        String json = gson.toJson(u, type);
        
        return json;
    }
    
    public static <T>String getObjectsJson(List<T> u){
        Gson gson = new Gson();
        Type type = new TypeToken<List<T>>() {}.getType();
        String json = gson.toJson(u, type);
        
        return json;
    }
}
