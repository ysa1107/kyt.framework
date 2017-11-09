package com.kyt.framework.util;

import com.google.gson.Gson;
import java.lang.reflect.Type;

public class JSONUtil
{
  public static final Gson json = new Gson();
  
  public static String Serialize(Object value)
  {
    return json.toJson(value);
  }
  
  public static <T> T DeSerialize(String value, Type typeOfT)
  {
    T result = json.fromJson(value, typeOfT);
    
    return result;
  }
}
