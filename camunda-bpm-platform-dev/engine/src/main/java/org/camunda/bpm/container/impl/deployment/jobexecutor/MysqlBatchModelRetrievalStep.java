package org.camunda.bpm.container.impl.deployment.jobexecutor;

import java.sql.*;
import java.util.Set;
import java.util.HashSet;

import org.camunda.bpm.engine.adapter.EngineAdapter;
import org.camunda.bpm.container.impl.spi.DeploymentOperation;
import org.camunda.bpm.container.impl.spi.DeploymentOperationStep;
import org.camunda.bpm.engine.messaging.MessageProducer;
import org.camunda.bpm.engine.messaging.Queue;

public class MysqlBatchModelRetrievalStep extends DeploymentOperationStep {

    private static Connection connection;

    public String getName() {
        return "Retrieving batch models";
    }

    @Override
    public void performOperationStep(DeploymentOperation operationContext){

        connect();
        EngineAdapter.INSTANCE.setList(getData());
    }

    public void connect(){

        final String url = "jdbc:mysql://localhost:3306/test";

        try{
            connection = DriverManager.getConnection(url, "root", "root");
        }
        catch(Exception e){
            MessageProducer.publish("Connection error: "+e.toString(), Queue.exception);
        }
    }

    public Set<String> getData(){

        Set<String> list = new HashSet<>();

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select activityId from models");

            while(rs.next()){
                list.add(rs.getString("activityId"));
            }
        }
        catch(Exception e){
            MessageProducer.publish("Data retrieval error: "+e.toString(), Queue.exception);
        }

        return list;
    }
}
