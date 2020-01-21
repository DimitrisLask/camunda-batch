package startup;

import clustering.BatchClusterManager;
import clustering.ProcessInstance;
import db.DBManager;
import messages.MessageReceiver;
import models.Model;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Singleton;

@Singleton
public class Startup {

    public static DBManager db;
    public static List<Model> models;
    public static final Logger log = LoggerFactory.getLogger("BATCH CONTROLLER");
    public static MessageReceiver receiver;
    public static List<ProcessInstance> globalList;

    public Startup(){

        //init components
        new BatchClusterManager();
        models = new ArrayList<>();
        db = new DBManager();
        receiver = new MessageReceiver();
        globalList = new ArrayList<>();
        db.fillLists();
    }
}
