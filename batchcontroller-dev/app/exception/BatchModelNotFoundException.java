package exception;

public class BatchModelNotFoundException extends Exception {

    public BatchModelNotFoundException(String process, String activity){
        super("No model could be found for process: "+process+" and activity: "+activity);
    }
}
