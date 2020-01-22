package org.camunda.bpm.engine.rest.extracts;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProcessActivityExtract{

    String id;
    String name;

    public ProcessActivityExtract(String id, String name){
        this.id = id;
        this.name = name;
    }
}

