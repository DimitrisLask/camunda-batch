package org.camunda.bpm.engine.rest.dto;

public class BatchActivityDto {

    private String processDefinitionId;
    private String activityId;
    private boolean block;

    public String getProcessDefinitionId(){
        return this.processDefinitionId;
    }

    public void setProcessDefinitionId(String id){
        this.processDefinitionId = id;
    }

    public String getActivityId(){
        return this.activityId;
    }

    public void setActivityId(String activityId){
        this.activityId = activityId;
    }

    public boolean isBlock(){
        return this.block;
    }

    public void setBlock(boolean block){
        this.block = block;
    }
}
