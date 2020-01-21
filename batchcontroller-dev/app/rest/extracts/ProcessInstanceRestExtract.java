package rest.extracts;

import clustering.ProcessInstance;

import java.util.Map;

public class ProcessInstanceRestExtract {

    protected String processInstanceId;
    protected Map<String, Object> variables;
    protected boolean resumeAtCurrentActivity;

    public ProcessInstanceRestExtract(){

    }

    public ProcessInstanceRestExtract(ProcessInstance instance){
        this.processInstanceId = instance.getProcessInstanceId();
        this.variables = instance.getVariables();
        this.resumeAtCurrentActivity = instance.getResumeAtCurrentActivity();
    }

    public String getProcessInstanceId(){
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Map<String, Object> getVariables() {
        return this.variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public boolean getResumeAtCurrentActivity() {
        return resumeAtCurrentActivity;
    }

    public void setResumeAtCurrentActivity(boolean resumeAtCurrentActivity) {
        this.resumeAtCurrentActivity = resumeAtCurrentActivity;
    }
}
