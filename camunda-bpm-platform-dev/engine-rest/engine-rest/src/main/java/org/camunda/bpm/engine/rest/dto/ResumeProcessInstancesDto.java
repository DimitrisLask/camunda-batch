package org.camunda.bpm.engine.rest.dto;

import java.util.Map;

public class ResumeProcessInstancesDto {

    //private String processDefinitionId;
    private Map<String, Map<String, Object>> processList;
    private boolean resumeAtCurrentActivity;

   /* public String getProcessDefinitionId(){
        return this.processDefinitionId;
    }*/

    public Map<String, Map<String, Object>> getProcessList(){
        return this.processList;
    }
    public boolean getResumeAtCurrentActivity(){
        return this.resumeAtCurrentActivity;
    }
}
