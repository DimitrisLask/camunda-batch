package clustering;

import exception.RemoveInstanceFromRunningBatchException;
import exception.RemoveRunningBatchException;
import models.Model;
import startup.Startup;

import java.util.*;

public class BatchClusterManager {

    private static Map<String, BatchCluster> batchClusters;
    private static Map<String, Integer> clusterKeys;
    private static Map<String, Timer> timers;

    public BatchClusterManager(){
        batchClusters = new HashMap<>();
        clusterKeys = new HashMap<>();
        timers = new HashMap<>();
    }

    public static String constructClusterKeyIdentifier(ProcessInstance instance, Model model){
        String key;
        String processDefinitionComponent;
        String processDefinitionIdParts[] = model.getProcessId().split(":");

        //If the process definition id contains the process version, include it in the processDefinitionComponent string
        if(processDefinitionIdParts.length == 3){
            processDefinitionComponent = processDefinitionIdParts[0]+":"+processDefinitionIdParts[1];
        }
        else{
            processDefinitionComponent = processDefinitionIdParts[0];
        }
        String activityComponent = model.getActivity();
        String attribute = model.getAttributes();
        String attributeValue = (String)instance.variables.get(attribute.toLowerCase());
        key = processDefinitionComponent+"/"+activityComponent+":"+attributeValue;

        return key;
    }

    public static BatchCluster getCluster(String clusterId){
        return batchClusters.get(clusterId);
    }

    public static Map<String, BatchCluster> getCLusters(){
        return batchClusters;
    }

    public static Map<String, Integer> getCLusterKeys(){
        return clusterKeys;
    }

    public static Map<String, Timer> getTimers(){
        return timers;
    }

    public static void assignToCluster(ProcessInstance instance){
        BatchCluster cluster = findOrCreate(instance);
        cluster.tryAdd(instance);
        instance.setClusterId(cluster.getId());
    }

    public ProcessInstance removeProcessInstanceFromCluster(ProcessInstance instance){

        BatchCluster cluster = getCluster(instance.getClusterId());

        try{
            if(!cluster.isRunning()){
                cluster.remove(instance);
            }
            else{
                throw new RemoveInstanceFromRunningBatchException();
            }
        }
        catch(RemoveInstanceFromRunningBatchException e){
            Startup.log.error(e.toString());
        }

        return instance;
    }

    private static BatchCluster findOrCreate(ProcessInstance instance){
        BatchCluster cluster = null;
        Model model = instance.getBatchModel();
        String key = constructClusterKeyIdentifier(instance, model);
        int clusterOrder = clusterKeys.getOrDefault(key, 0);

        if(clusterOrder == 0){
            cluster = new BatchCluster(model);
            cluster.setKey(key);
            addCluster(cluster);
            clusterKeys.put(key, ++clusterOrder);
            startBatchTimer(cluster);
            Startup.log.info("New batch with id: "+cluster.getId()+" started");
        }
        else{
            String clusterId = key+"-"+clusterOrder;
            cluster = batchClusters.get(clusterId);
        }

        return cluster;
    }

    public static void addCluster(BatchCluster cluster){
        batchClusters.put(cluster.getId(), cluster);
    }

    public static void removeCluster(BatchCluster cluster){

        try{
            if(cluster.isRunning()){
                throw new RemoveRunningBatchException();
            }
            cluster.decrease();
            batchClusters.remove(cluster.getId());
        }
        catch(Exception e){
            Startup.log.error(e.toString());
        }
    }

    public static void startBatchTimer(BatchCluster cluster){
        Timer timer = new Timer();
        timers.put(cluster.getId(), timer);

        try{
            long delay = cluster.transformToLong();
            timer.schedule(cluster.startTimer(), delay);
        }
        catch(Exception e){
            Startup.log.error(e.toString()+" : "+e.getMessage());
        }
    }

    public static void cancelBatch(BatchCluster cluster){
        Timer timer = timers.get(cluster.getId());
        try{
            timer.cancel();
        }
        catch(Exception e){
            Startup.log.error(e.toString()+" : "+e.getMessage());
        }
        finally{
            timers.remove(cluster.getId());
            cluster.finalizeBatch(true);
        }
    }

    public static void updateBatchClustersByModel(Model model){

        BatchCluster cluster;

        for(String key : batchClusters.keySet()){
            cluster = batchClusters.get(key);
            if(cluster.getModel().getId().equals(model.getId())){
                cluster.setModel(model);
            }
        }
    }

    public static void removeAndFinalize(Model model){

    }
}
