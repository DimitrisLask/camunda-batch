package messages.handler;

import clustering.BatchClusterManager;
import clustering.ProcessInstance;
import controllers.ProcessController;
import messages.json.JsonBuilder;
import startup.Startup;

public class BlockMessageHandler implements Handler {

    public static void handle(String message){
        ProcessInstance instance;
        try{
            instance = (ProcessInstance) JsonBuilder.fromJson(message, ProcessInstance.class);
            instance.repairVariables();
            ProcessController.addToList(instance);
            Startup.globalList.add(instance);
            BatchClusterManager.assignToCluster(instance);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
