package org.camunda.bpm.engine.rest.dto;

import java.util.Map;

public class ResumeProcessInstanceDto {

    protected String processInstanceId;
    //protected Map<String, VariableValueDto> variables;
    protected Map<String, Object> variables;
    protected boolean resumeAtCurrentActivity;

    public String getProcessInstanceId(){
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId){
        this.processInstanceId = processInstanceId;
    }

    public Map<String, Object> getVariables(){
        return this.variables;
    }

    public void setVariables(Map<String, Object> variables){
        this.variables = variables;
    }

    /*public Map<String, VariableValueDto> getVariables(){
        return this.variables;
    }

    public void setVariables(Map<String, VariableValueDto> variables){
        this.variables = variables;
    }*/
    public boolean getResumeAtCurrentActivity(){
        return this.resumeAtCurrentActivity;
    }

    public void setResumeAtCurrentActivity(boolean resumeAtCurrentActivity){
        this.resumeAtCurrentActivity = resumeAtCurrentActivity;
    }
}
