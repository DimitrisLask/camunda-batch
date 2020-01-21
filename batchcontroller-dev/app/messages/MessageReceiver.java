package messages;

import com.rabbitmq.client.*;
import messages.handler.BlockMessageHandler;
import messages.handler.ExceptionMessageHandler;
import messages.handler.InfoMessageHandler;
import startup.Startup;

import javax.xml.stream.events.StartElement;
import java.io.IOException;
import java.util.logging.Logger;


public class MessageReceiver {

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private static final Logger LOGGER = Logger.getLogger(MessageReceiver.class.getName());

    public MessageReceiver(){

        factory = new ConnectionFactory();
        factory.setHost("localhost");

        try{
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare("batching", "direct", false, false, null);
            channel.queueDeclare("info", false, false, false, null);
            channel.queueDeclare("block", false, false, false, null);
            channel.queueDeclare("exception", false, false, false, null);
            channel.queueBind("info", "batching", "info");
            channel.queueBind("block", "batching", "block");
            channel.queueBind("exception", "batching", "exception");
        }
        catch(IOException io){
            System.out.println(io.getMessage());
        }

        Startup.log.info("Message Receiver successfully started. Waiting for messages...");
        //System.out.println(" [*] Waiting for messages");
        handleMessages();
    }

    public Channel getChannel(){
        return this.channel;
    }

    public void handleMessages(){

        try{
            channel.basicConsume("info", false, "InfoConsumer",
                    new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException
                        {
                            String routingKey = envelope.getRoutingKey();
                            String contentType = properties.getContentType();
                            long deliveryTag = envelope.getDeliveryTag();
                            String message = new String(body, "UTF-8");
                            InfoMessageHandler.handle(message);
                            channel.basicAck(deliveryTag, false);
                        }
                    });
            channel.basicConsume("block", false, "BlockConsumer",
                    new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException
                        {
                            String routingKey = envelope.getRoutingKey();
                            String contentType = properties.getContentType();
                            long deliveryTag = envelope.getDeliveryTag();
                            String message = new String(body, "UTF-8");
                            BlockMessageHandler.handle(message);
                            channel.basicAck(deliveryTag, false);
                        }
                    });
            channel.basicConsume("exception", false, "ExceptionConsumer",
                    new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException
                        {
                            String routingKey = envelope.getRoutingKey();
                            String contentType = properties.getContentType();
                            long deliveryTag = envelope.getDeliveryTag();
                            String message = new String(body, "UTF-8");
                            ExceptionMessageHandler.handle(message);
                            channel.basicAck(deliveryTag, false);
                        }
                    });
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
