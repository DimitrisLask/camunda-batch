package org.camunda.bpm.engine.messaging;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.adapter.BatchInstance;

public class JsonBuilder {

    private static ObjectMapper mapper;
    private static final JsonBuilder INSTANCE = new JsonBuilder();

    private JsonBuilder(){
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static String toJson(BatchInstance instance){

        String result = null;

        try{
            result = mapper.writeValueAsString(instance);
        }
        catch(Exception e){
            result = e.toString();
            MessageProducer.publish(result, Queue.exception);
        }

        return result;
    }

    public static BatchInstance fromJson(String json){

        BatchInstance instance = null;

        try{
            instance = mapper.readValue(json, BatchInstance.class);
        }
        catch(Exception e){
            MessageProducer.publish(e.toString(), Queue.exception);
        }

        return instance;
    }
}
