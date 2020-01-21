package messages.handler;


public class ExceptionMessageHandler implements Handler {

    public static void handle(String message){
        System.out.println("[CAMUNDA][EXCEPTION]-> '" + message + "'");
    }
}
