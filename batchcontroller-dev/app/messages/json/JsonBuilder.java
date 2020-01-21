package messages.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import startup.Startup;

public class JsonBuilder {

    private static ObjectMapper mapper;
    private static final JsonBuilder INSTANCE = new JsonBuilder();

    private JsonBuilder(){
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC);
    }


    public static String toJson(Object o){
        String json = null;

        try{
            json = mapper.writeValueAsString(o);
        }
        catch(Exception e){
            Startup.log.error(e.toString());
        }

        return json;
    }

    public static Object fromJson(String json, Class<?> type){
        Object o = null;

        try{
            o = mapper.readValue(json, type);
        }
        catch(Exception e){
            Startup.log.error(e.toString());
        }

        return o;
    }
}
