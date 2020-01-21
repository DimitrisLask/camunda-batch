package delegate;

import clustering.BatchCluster;
import clustering.ProcessInstance;
import models.Model;
import startup.Startup;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class BatchExecutionManager {

    private static final String delegatePackage = "delegate.definitions.";

    public BatchExecutionManager() {
    }

    /*public static void start(BatchCluster cluster, CountDownLatch latch) {

        String activity = cluster.getModel().getActivity().replace(" ", "");

       try{
           Class<?> clazz = Class.forName(delegatePackage+activity);
           if(cluster.getModel().getOrder() == Model.executionOrder.PARALLEL){
               executeParallel(cluster, clazz, latch);
           }
           else{
               executeSequential(cluster, clazz);
           }
       }
       catch(Exception e){
           e.printStackTrace();
       }

    }*/

    public static void executeParallel(BatchCluster cluster, CountDownLatch latch) {
        String activity = cluster.getModel().getActivity().replace(" ", "");

        try{
            Class<?> clazz = Class.forName(delegatePackage+activity);
            Constructor<?> delegateConstructor = clazz.getConstructor(Map.class);
            for(ProcessInstance instance : cluster.getList()){
                new InstanceExecutor(instance.getVariables(), delegateConstructor, latch);
            }
        }
        catch(Exception e){
            Startup.log.error(e.toString());
        }
    }

    public static void executeSequential(BatchCluster cluster) {

    }
}