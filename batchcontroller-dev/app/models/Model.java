package models;

public class Model {

    private String id;
    private String name;                        // REQUIRED
    private int maxBatchSize;                   // REQUIRED
    private int minBatchSize;                   // Activation rule REQUIRED
    private double timeLimit;                   // Activation rule REQUIRED
    private hourEnum time;                      // Activation rule REQUIRED
    private executionOrder order;
    private String process;                     // process name REQUIRED
    private String processId;
    private String activity;
    private String activityId;
    private String attributes;

    public Model(){

    }

    public Model(String id, String name, int maxBatchSize, int minBatchSize, double timeLimit, hourEnum time,
                 executionOrder order, String process, String processId, String activity, String activityId, String attributes){

        this.id = id;
        this.name = name;
        this.maxBatchSize = maxBatchSize;
        this.minBatchSize = minBatchSize;
        this.timeLimit = timeLimit;
        this.time = time;
        this.order = order;
        this.process = process;
        this.processId = processId;
        this.activity = activity;
        this.activityId = activityId;
        this.attributes = attributes;
    }

    public Model(String id, String name, int maxBatchSize, int minBatchSize, double timeLimit, hourEnum time, String process, String processId, String activity, String activityId){

        this.id = id;
        this.name = name;
        this.maxBatchSize = maxBatchSize;
        this.minBatchSize = minBatchSize;
        this.timeLimit = timeLimit;
        this.time = time;
        this.order = executionOrder.PARALLEL;
        this.process = process;
        this.processId = processId;
        this.activity = activity;
        this.activityId = activityId;
        this.attributes = null;
    }

    public Model(Model newModel){

        this.id = newModel.id;
        this.name = newModel.name;
        this.maxBatchSize = newModel.maxBatchSize;
        this.minBatchSize = newModel.minBatchSize;
        this.timeLimit = newModel.timeLimit;
        this.time = newModel.time;
        this.order = newModel.order;
        this.process = newModel.process;
        this.processId = newModel.processId;
        this.activity = newModel.activity;
        this.activityId = newModel.activityId;
        this.attributes = newModel.attributes;

    }

    public enum executionOrder{
        PARALLEL,
        SEQUENTIAL

    }

    public enum hourEnum{
        HOURS,
        MINUTES,
        SECONDS
    }

    public String createValueString(){
        return "('"+this.name+"', "+
                this.maxBatchSize+", "+
                this.minBatchSize+", "+
                this.timeLimit+", '"+
                this.time.toString()+"', '"+
                this.order.toString()+"', "+
                "'"+this.process+"', "+
                "'"+this.processId+"', "+
                "'"+this.activity+"', "+
                "'"+this.activityId+"', "+
                "'"+this.attributes+"', " +
                "'"+this.id+"');";
    }

    public static Model update(Model newModel){
        return new Model(newModel);
    }

    public String getId(){return this.id;}
    public void setId(String id){this.id = id;}

    public String getName(){return this.name;}

    public int getMaxBatchSize(){return this.maxBatchSize;}

    public int getMinBatchSize(){return this.minBatchSize;}

    public double getTimeLimit(){return this.timeLimit;}

    public hourEnum getTime(){return this.time;}

    public executionOrder getOrder() {return this.order;}

    public String getProcess(){return this.process;}

    public String getProcessId(){return this.processId;}

    public String getActivity(){return this.activity;}
    public String getActivityId(){
        return this.activityId;
    }

    public String getAttributes(){return this.attributes;}
}
