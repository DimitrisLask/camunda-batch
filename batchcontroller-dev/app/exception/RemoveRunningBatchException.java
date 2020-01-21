package exception;

public class RemoveRunningBatchException extends Exception {

    public RemoveRunningBatchException(){
        super("Cannot delete batch while it is running");
    }
}
