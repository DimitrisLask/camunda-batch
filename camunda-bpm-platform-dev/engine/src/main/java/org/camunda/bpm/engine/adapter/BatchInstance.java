package org.camunda.bpm.engine.adapter;

import java.util.HashMap;
import java.util.Map;

public class BatchInstance {

    private String processInstanceId;
    private String parentId;
    private String processDefinitionId;
    private String processDefinition;
    private String currentActivityId;
    private String activity;
    private Map<String, Object> variables;

    public BatchInstance(String processInstanceId, String parentId, String processDefinitionId, String processDefinition, String currentActivityId, String activity, Map<String, Object> variables){

        this.processInstanceId = processInstanceId;
        this.parentId = parentId;
        this.processDefinitionId = processDefinitionId;
        this.processDefinition = processDefinition;
        this.currentActivityId = currentActivityId;
        this.activity = activity;
        this.variables = new HashMap<String, Object>(variables);
    }
}
