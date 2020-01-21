package exception;

public class RemoveInstanceFromRunningBatchException extends Exception {

    public RemoveInstanceFromRunningBatchException(){
        super("Cannot Remove Process Instance While Batch Is Running");
    }
}
