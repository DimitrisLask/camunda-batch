package org.camunda.bpm.engine.rest.extracts;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProcessDefinitionExtract{

    private String id;
    private String name;
    private List<ProcessActivityExtract> activityList;

    public ProcessDefinitionExtract(String id, String name, List<ProcessActivityExtract> list){

        this.id = id;
        this.name = name;
        this.activityList = list;
    }

    public ProcessDefinitionExtract(String id, String name){

        this.id = id;
        this.name = name;
        this.activityList = null;
    }
}
