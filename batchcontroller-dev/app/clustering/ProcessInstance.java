package clustering;


import models.Model;
import startup.Startup;

import java.util.HashMap;
import java.util.Map;

public class ProcessInstance {

    protected String processInstanceId;
    protected String parentId;
    protected String processDefinitionId;
    protected String processDefinition;
    protected String currentActivityId;
    protected String activity;
    protected Map<String, Object> variables;
    protected String clusterId;
    protected boolean resumeAtCurrentActivity;

    public ProcessInstance(){

    }

    public ProcessInstance(String id, String parentId, String definitionId, String processDefinition, String currentActivityId, String activity, Map<String, Object> variables){

        this.processInstanceId = id;
        this.parentId = parentId;
        this.processDefinitionId = definitionId;
        this.processDefinition = processDefinition;
        this.currentActivityId = currentActivityId;
        this.activity = activity;
        this.variables = new HashMap<>(variables);
    }

    Model getBatchModel(){

        return   Startup.models.stream()
                .filter(item -> item.getProcessId().equals(this.processDefinitionId) && item.getActivityId().equals(this.currentActivityId))
                .findFirst()
                .orElse(null);
    }

    public Map<String, Object> getVariables(){
        return this.variables;
    }

    public void setVariables(Map<String, Object> variables){
        this.variables = variables;
    }

    public String getProcessInstanceId(){
        return this.processInstanceId;
    }

    public String getParentId(){
        return this.parentId;
    }

    public String getProcessDefinition(){
        return this.processDefinition;
    }

    public String getProcessDefinitionId(){
        return this.processDefinitionId;
    }

    public String getCurrentActivityId(){
        return this.currentActivityId;
    }

    public void setClusterId(String id){
        this.clusterId = id;
    }

    public String getClusterId(){
        return this.clusterId;
    }

    public boolean getResumeAtCurrentActivity(){
        return this.resumeAtCurrentActivity;
    }

    public void setResumeAtCurrentActivity(boolean set){
        this.resumeAtCurrentActivity = set;
    }

    public void removeFromCluster(){

        try{
            BatchCluster cluster = BatchClusterManager.getCluster(this.clusterId);
            cluster.remove(this);
        }
        catch(Exception e){
            System.out.println(e.toString()+" : "+e.getMessage());
        }
    }

    // Casting Long to Integer is needed after Jackson deserialization
    public void repairVariables(){

        Object o = null;
        for(String str : this.variables.keySet()){
            Class<?> clazz = this.variables.get(str).getClass();
            if(clazz == Integer.class){
                o = this.variables.get(str);
            }
            try{
                o = (long) (int) o;
                this.variables.put(str, o);
            }
            catch(Exception e){
                Startup.log.error(e.toString());
            }
        }
    }
}
