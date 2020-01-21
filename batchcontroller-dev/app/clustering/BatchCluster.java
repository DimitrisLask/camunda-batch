package clustering;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import delegate.BatchExecutionManager;
import exception.RemoveInstanceFromRunningBatchException;
import messages.json.JsonBuilder;
import models.Model;
import rest.RESTClient;
import rest.extracts.ProcessInstanceListRestExtract;
import startup.Startup;

public class BatchCluster {

    private String id;
    private String key;
    private List<ProcessInstance> instances;
    private Model batchModel;
    private int max;
    private int min;
    private int currentCapacity;
    private boolean running = false;
    private static int counter = 0;

    public BatchCluster(Model model){

        instances = new ArrayList<>();
        this.max = model.getMaxBatchSize();
        this.min = model.getMinBatchSize();
        this.currentCapacity = 0;
        this.batchModel = model;
        counter++;
    }

    public void setKey(String key){
        this.key = key;
        this.setId(key);
    }

    private String getKey(){
        return this.key;
    }

    public int getCurrentCapacity(){
        return this.currentCapacity;
    }

    public List<ProcessInstance> getList(){
        return this.instances;
    }

    public void tryAdd(ProcessInstance instance){

        if(this.currentCapacity < this.batchModel.getMaxBatchSize()){
            this.instances.add(instance);
            increaseCurrentCapacity();
        }
        else{
            BatchCluster newCluster;
            try{
                newCluster = new BatchCluster(this.batchModel);
                newCluster.setKey(this.key);
                newCluster.instances.add(instance);
                BatchClusterManager.addCluster(newCluster);
                int nextBatchNumber = BatchClusterManager.getCLusterKeys().get(this.key);
                BatchClusterManager.getCLusterKeys().put(this.key, nextBatchNumber);
                BatchClusterManager.startBatchTimer(newCluster);
                //this.startTimer().run();
            }
            catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }

    public void remove(ProcessInstance instance){

        try{
            if(!this.isRunning()){
                this.instances.remove(instance);
                --this.currentCapacity;
            }
            else{
                throw new RemoveInstanceFromRunningBatchException();
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public String getId(){
        return this.id;
    }

    private void setId(String key){
        int order = BatchClusterManager.getCLusterKeys().getOrDefault(key, 0);
        ++order;
        this.id = key+"-"+order;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public boolean isRunning(){
        return this.running;
    }

    public int count(){
        return counter;
    }

    void increaseCurrentCapacity(){
        ++this.currentCapacity;
        if(!(this.currentCapacity<this.batchModel.getMaxBatchSize())){
            Timer timer = BatchClusterManager.getTimers().get(this.id);
            timer.cancel();
            this.startTimer().run();
        }

    }

    public void decrease(){
        counter--;
    }

    public TimerTask startTimer(){

        BatchCluster cluster = this;
        TimerTask task;
        task = new TimerTask() {
            @Override
            public void run() {
                if(cluster.currentCapacity < min){
                    Startup.log.info("Minimum number of instances has not been reached. Resuming individual execution...");
                    BatchClusterManager.cancelBatch(cluster);
                    cancel();
                }
                else{
                    Startup.log.info("Execution for Batch "+cluster.id+" has started");
                    cluster.setRunning(true);
                        try{
                            if(cluster.getModel().getOrder() == Model.executionOrder.PARALLEL){
                                CountDownLatch latch = new CountDownLatch(cluster.instances.size());
                                BatchExecutionManager.executeParallel(cluster, latch);
                                latch.await();
                            }
                            else{
                                BatchExecutionManager.executeSequential(cluster);
                            }
                            Startup.log.info("Execution for Batch "+cluster.id+" has finished");
                            cluster.finalizeBatch(false);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                }
            }
        };

        return task;
    }

    long transformToLong(){

        long multiplier;
        double delay = this.batchModel.getTimeLimit();

        if(this.batchModel.getTime() == Model.hourEnum.HOURS){
            multiplier = 3600*1000L;
        }
        else if(this.batchModel.getTime() == Model.hourEnum.MINUTES){
            multiplier = 60*1000L;
        }
        else{
            multiplier = 1000L;
        }

        return (long)delay*multiplier;
    }

    public void finalizeBatch(boolean resumeAtCurrentActivity) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        this.setRunning(false);

        for (ProcessInstance instance : this.instances) {
            Startup.globalList.remove(instance);
            map.put(instance.processInstanceId, instance.variables);
        }

        BatchClusterManager.removeCluster(this);

        int oldOrder = BatchClusterManager.getCLusterKeys().get(this.key);
        int newOrder = --oldOrder;
        if(newOrder != 0){
            BatchClusterManager.getCLusterKeys().put(this.key, newOrder);
        }
        else{
            BatchClusterManager.getCLusterKeys().remove(this.key);
        }


        String json = JsonBuilder.toJson(new ProcessInstanceListRestExtract(map, resumeAtCurrentActivity));
        String method = "process-instance/resumeMany";
        RESTClient.newRequest(json, method);
    }

    public Model getModel(){
        return this.batchModel;
    }

    public void setModel(Model model){
        this.batchModel = model;
    }

}
