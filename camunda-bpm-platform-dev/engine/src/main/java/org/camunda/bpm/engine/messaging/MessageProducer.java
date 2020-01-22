package org.camunda.bpm.engine.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.logging.Logger;

public class MessageProducer {

    private ConnectionFactory factory;
    private Connection connection;
    private static Channel channel;
    private static final Logger LOGGER = Logger.getLogger(MessageProducer.class.getName());
    private static MessageProducer instance = new MessageProducer();

    private MessageProducer(){

        factory = new ConnectionFactory();
        factory.setHost("localhost");

        try{
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare("batching", "direct", false, false, null);
            //channel.queueDeclare("test", false, false, false, null);
            channel.queueDeclare("info", false, false, false, null);
            channel.queueDeclare("block", false, false, false, null);
            channel.queueDeclare("exception", false, false, false, null);
            channel.queueBind("info", "batching", "info");
            channel.queueBind("block", "batching", "block");
            channel.queueBind("exception", "batching", "exception");
        }
        catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }

    public static MessageProducer getInstance(){
        return MessageProducer.instance;
    }

    public Channel getChannel(){
        return this.channel;
    }

    public static void publish(String message, Queue queue){

        try{
            channel.basicPublish("batching", queue.getName(), null, message.getBytes("UTF-8"));
        }
        catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
    }
}
