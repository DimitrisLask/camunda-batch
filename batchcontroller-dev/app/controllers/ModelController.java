package controllers;

import clustering.BatchClusterManager;
import messages.json.JsonBuilder;
import models.Model;
import play.mvc.*;
import rest.RESTClient;
import startup.Startup;
import javax.inject.Singleton;
import java.util.Iterator;
import java.util.ListIterator;

@Singleton
public class ModelController extends Controller {

    private static Iterator<Model> modelsIt;
    private static ListIterator<Model> listIt;

    public Result getModels(){

        Result result;
        try{
            String response = JsonBuilder.toJson(Startup.models);
            result = ok(response);
        }
        catch(Exception e){
            result = internalServerError("Server error: Could not get model list\nReason: "+e.toString());
        }

        return result;
    }

    public Result createModel(String json){

        Result result = null;
        String success;

        try{
            Model model = (Model)JsonBuilder.fromJson(json, Model.class);
            success = Boolean.toString(Startup.db.create(model.createValueString()));
            if(success.equals("true")){
                Startup.models.add(model);
                result = ok("Model successfully created");
                String jsonRest = "{\"processDefinitionId\" : \""+model.getProcessId()+"\", \"activityId\" : \""+model.getActivityId()+"\", \"block\" : \"true\"}";
                String method = "process-definition/set-batch-activity";
                RESTClient.newRequest(jsonRest, method);
            }
        }
        catch(Exception e){
            result = internalServerError("Server error: Could not create model\n Reason: "+e.toString());
        }

        return result;
    }

    public Result updateModel(String json){

        Result result = null;
        String success;
        try{
            Model model = (Model)JsonBuilder.fromJson(json, Model.class);
            success = Boolean.toString(Startup.db.update(model));
            if(success.equals("true")){
                listIt = Startup.models.listIterator();
                while(listIt.hasNext()){
                    Model m = listIt.next();
                    if(m.getId().equals(model.getId())){
                        listIt.set(model);
                        BatchClusterManager.updateBatchClustersByModel(model);
                    }

                }

                result = ok("Model successfully updated");
            }

            String jsonRest = "{\"processDefinitionId\" : \""+model.getProcessId()+"\", \"activityId\" : \""+model.getActivityId()+"\", \"block\" : \"true\"}";
            String method = "process-definition/set-batch-activity";
            RESTClient.newRequest(jsonRest, method);
        }
        catch(Exception e){
            result = internalServerError("Server error: Could not update model\n Reason: "+e.toString());
        }

        return result;
    }

    public Result deleteModel(String id){

        Result result = null;

        try{
            Model m = null;
            String response = Boolean.toString(Startup.db.delete(id));
            if (response.equals("true")){
                modelsIt = Startup.models.iterator();
                while(modelsIt.hasNext()){
                    m = modelsIt.next();
                    if(m.getId().equals(id)){
                        modelsIt.remove();
                        break;
                    }
                }

                if(m != null){
                    result = ok("Model successfully deleted");
                    String jsonRest = "{\"processDefinitionId\" : \""+m.getProcessId()+"\", \"activityId\" : \""+m.getActivityId()+"\", \"block\" : \"false\"}";
                    String method = "process-definition/set-batch-activity";
                    RESTClient.newRequest(jsonRest, method);
                    //BatchClusterManager.removeAndFinalize(m);
                }

            }
            else{
                result = internalServerError("Server error: Could not delete model");
            }
        }
        catch(Exception e){
            result = internalServerError("Server error: Could not delete model\n Reason: "+e.toString());
        }
        return result;
    }
}
