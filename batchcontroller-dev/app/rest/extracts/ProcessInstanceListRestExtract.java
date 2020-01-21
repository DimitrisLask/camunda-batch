package rest.extracts;

import java.util.Map;

public class ProcessInstanceListRestExtract {

    protected Map<String, Map<String, Object>> processList;
    private boolean resumeAtCurrentActivity;

    public ProcessInstanceListRestExtract(){

    }

    public ProcessInstanceListRestExtract(Map<String, Map<String, Object>> processList, boolean resumeAtCurrentActivity){
        this.processList = processList;
        this.resumeAtCurrentActivity = resumeAtCurrentActivity;
    }

    public Map<String, Map<String, Object>> getProcessList(){
        return this.processList;
    }

    public void setProcessList(Map<String, Map<String, Object>> processList){
        this.processList =processList;
    }

    public boolean getResumeAtCurrentActivity(){
        return this.resumeAtCurrentActivity;
    }

    public void setResumeAtCurrentActivity(boolean resumeAtCurrentActivity){
        this.resumeAtCurrentActivity = resumeAtCurrentActivity;
    }
}
