package db;
import java.sql.*;

import clustering.ProcessInstance;
import models.Model;
import startup.Startup;


public class DBManager {

    private static Connection connection;
    private static final String url = "jdbc:mysql://localhost:3306/test";

    public DBManager(){
        connect();
    }

    private void connect(){

        try{
            connection = DriverManager.getConnection(url, "root", "root");
            Startup.log.info("Successfully connected to database");
        }
        catch(Exception e){
            Startup.log.error(e.getMessage());
        }
    }
    public void fillLists(){

        try{
            Statement stmt=connection.createStatement();
            ResultSet rs1=stmt.executeQuery("select * from models");
            while(rs1.next()){
                Model model = new Model(
                        rs1.getString("Id"),
                        rs1.getString("name"),
                        rs1.getInt("maxBatchSize"),
                        rs1.getInt("minBatchSize"),
                        rs1.getInt("timeLimit"),
                        Model.hourEnum.valueOf(rs1.getString("time")),
                        Model.executionOrder.valueOf(rs1.getString("executionOrder")),
                        rs1.getString("process"),
                        rs1.getString("processId"),
                        rs1.getString("activity"),
                        rs1.getString("activityId"),
                        rs1.getString("attributes"));
                Startup.models.add(model);
            }

        }
        catch(Exception e){
            Startup.log.error(e.getMessage());
        }
    }

    public boolean update(Model model){

        boolean success = false;

        try{
            PreparedStatement ps = connection.prepareStatement("update models set " +
                    "name='"+model.getName()+"'," +
                    "maxBatchSize="+model.getMaxBatchSize()+","+
                    "minBatchSize="+model.getMinBatchSize()+","+
                    "timeLimit="+model.getTimeLimit()+","+
                    "time='"+model.getTime().toString()+"',"+
                    "executionOrder='"+model.getOrder().toString()+"',"+
                    "process='"+model.getProcess()+"',"+
                    "processId='"+model.getProcessId()+"',"+
                    "activity='"+model.getActivity()+"',"+
                    "attributes='"+model.getAttributes()+"' "+
                    "where Id='"+model.getId()+"';");
           success = (ps.executeUpdate()==1);
        }
        catch(Exception e){
            Startup.log.error(e.getMessage());
        }

        return success;
    }

    public boolean delete(String id){

        boolean success;
        try{
            Statement stmt=connection.createStatement();
            int rs=stmt.executeUpdate("delete from models where id='"+ id+"'");
            success = (rs==1);
        }
        catch(Exception e){
            success = false;

            e.printStackTrace();
        }
        return success;
    }

    public boolean create(String modelValues){

        boolean success = false;

        try{
            Statement stmt=connection.createStatement();
            int rs=stmt.executeUpdate("insert into models (name, maxBatchSize, minBatchSize, timeLimit, time," +
                    " executionOrder, process, processId, activity, activityId, attributes, Id)" +
                    " values"+modelValues);
            success = (rs==1);
        }
        catch(Exception e){
            Startup.log.error("Create: " +e.getMessage());
        }

        return success;
    }

}
