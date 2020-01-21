package controllers;

import clustering.BatchCluster;
import clustering.BatchClusterManager;
import clustering.ProcessInstance;
import exception.ProcessInstanceNotFoundException;
import messages.json.JsonBuilder;
import org.apache.http.NoHttpResponseException;
import play.mvc.*;
import rest.extracts.ProcessInstanceRestExtract;
import rest.RESTClient;
import startup.Startup;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProcessController extends Controller {

    private static List<ProcessInstance> blockedProcessesList = new ArrayList<>();

    public static void addToList(ProcessInstance instance){
            blockedProcessesList.add(instance);
    }

    public Result sendList(){

        Result result;
        String message;

        if(!blockedProcessesList.isEmpty()){
            try{
                message = JsonBuilder.toJson(blockedProcessesList);
                blockedProcessesList.clear();
                result = ok(message);
            }
            catch(Exception e){
                e.printStackTrace();
                result = internalServerError(e.toString()+" : "+e.getMessage());
            }
        }
        else{
            result = internalServerError("List is empty");
        }

        return result;
    }

    public Result resume(String processInstanceId){

        Result result;
        int code;
        ProcessInstance instance = null;

        try{
            instance = Startup.globalList
                    .stream()
                    .filter(item -> item.getProcessInstanceId().equals(processInstanceId) || (item.getParentId() != null && item.getParentId().equals(processInstanceId)))
                    .findAny()
                    .orElse(null);
            if(instance != null){
                instance.removeFromCluster();
                Startup.globalList.remove(instance);
                instance.setResumeAtCurrentActivity(true);
                ProcessInstanceRestExtract extract = new ProcessInstanceRestExtract(instance);
                String json = JsonBuilder.toJson(extract);
                String method = "process-instance/resumeOne";
                code = RESTClient.newRequest(json, method);

                if(code > 299){
                    throw new NoHttpResponseException("Server error");
                }
            }
            else{
                throw new ProcessInstanceNotFoundException("Process instance not found");
            }

            result = ok("Process successfully resumed");
        }
        catch(Exception e){
            result = internalServerError(e.toString());
        }

        return result;
    }
}
