package messages.handler;

public class InfoMessageHandler {

    public static void handle(String message){
        System.out.println("[CAMUNDA][INFO] -> "+message);
    }
}
