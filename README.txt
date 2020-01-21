******************************************* CONTENTS *******************************************

This folder contains the following:

1) A folder named "batchcontroller-app" containing the executable files of the Batch Controller.

2) A folder named "camunda-bpm-platform-app" containing the executable files of Camunda engine and Tomcat server

3) A folder named "BatchManagement-app" containing the source files of the web page for the batch management tool
as well as the page itself called "index.html". The latter can be found in .\BatchManagement-app\app\

4) A folder named "batchcontroller-dev" containing the source code and IntelliJ IDEA project structure of the 
Batch Controller app

5) A folder named "camunda-bpm-platform-dev" containing the source code of Camunda BPM community version as downloaded 
from Camunda Github repository. The source code also contains the batch modifications.

6) A "start.bat" file.

******************************************* HOW TO RUN *******************************************

1) Java must be installed in your device.

2) Before starting up the system make sure to have a MySQL instance and RabbitMQ installed locally.

3) MySQL needs to have a database called "test" and must ba accessed using "root" and "root" as username and password. 
This database must contain one table called "models". You can create it using the following query:

CREATE TABLE `models`(

`name` varchar(40) NOT NULL,

`maxBatchSize` int(11) DEFAULT NULL,

`minBatchSize` int(11) DEFAULT NULL,

`timeLimit` decimal(10,0) DEFAULT NULL,

`time` enum('HOURS','MINUTES','SECONDS') DEFAULT NULL,

`executionOrder` enum('PARALLEL','SEQUENTIAL') DEFAULT NULL,

`process` varchar(40) DEFAULT NULL,

`processId` varchar(60) DEFAULT NULL,

`activity` varchar(45) DEFAULT NULL,

`activityId` varchar(45) DEFAULT NULL,

`attributes` text,

`Id` varchar(60) NOT NULL,

PRIMARY KEY (`Id`)
) 
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


Otherwise, you can create your own credentials and table but the connection details must also be modified in 
.\batchcontroller-dev\app\db\DBManager.java

4) The system should automatically create three RabbitMQ message queues on startup, provided that the RabbitMQ 
server is already running. To see what queues currently exist after installing the node, type "http://localhost:15672"
in the browser.

5) After ensuring the above, double-click "start.bat" file to start Camunda, Batch Controller and 
the Batch Management Tool. Camunda, Tomcat and Batch Controller will take some time to start, so the web page
has to be refreshed before acquiring the data.


******************************************* TROUBLESHOOTING *******************************************

1) Camunda runs on Tomcat on port 8080. Batch Controller runs on port 9000 and RabbitMQ runs on port 15672. Every HTTP 
call is made on localhost. These calls should not fail, but if they do, check the url of the request in the respective 
files. For the web page these calls are located in the Javascript code at 
.\camunda-batch\BatchManagement-app\app\Controller.js. HTTP calls from the Batch Controller to Camunda are located at
.\camunda-batch\batchcontroller-dev\app\rest\RESTClient.java

2) The Batch Controller may fail to start with an error message stating that the server is already running. In this case 
go to .\camunda-batch\batchcontroller-app\bin\ and search for a RUNNING_PID file, then delete it and start the system 
again.

3) Some Camunda apps may produce errors and fail to load certain modules. In this case reload the page again and it 
should work.

4) When building Camunda make sure to copy .\camunda-batch\camunda-bpm-platform-dev\web.xml to the "conf" folder of 
the Tomcat folder created by the build, in order to allow HTTP calls.

5) For Camunda related errors it may be easier to consult the online forum. https://forum.camunda.org/


******************************************* DEVELOPMENT *******************************************

1) It is strongly suggested to use IntelliJ IDEA as the IDE for the projects of Camuda engine and Batch Controller.

2) Camunda must be imported as a Maven project.

3) In order to build Camunda go to the root folder of the project, then open a command-line session there 
(Shift+Right-click) and type "mvn -e clean package". You can also add "-Dmaven.test.skip=true" at the end to skip the 
tests in order to build it faster.

4) This is a list of all the Camunda classes that have received modifications for the project:


I) .\camunda-bpm-platform-dev\engine-rest

   1) .\camunda-batch\camunda-bpm-platform-dev\engine-rest\engine-rest\src\main\java\org\camunda\bpm\engine\rest
       
      ProcessDefinitionRestService.java: getProcessActivityNames() and setbatchActivity()
      ProcessInstanceRestService.java: resumeProcessInstanceById() and resumeProcessInstanceByList()

   2) .\camunda-batch\camunda-bpm-platform-dev\engine-rest\engine-rest\src\main\java\org\camunda\bpm\engine\rest\impl

      ProcessDefinitionRestServiceImpl.java: getProcessActivityNames(), setbatchActivity() and getProcessActivitiesByProcessId()
      ProcessInstanceRestServiceImpl.java: resumeProcessInstanceById() and resumeProcessInstanceByList()


II) .\camunda-bpm-platform-dev\engine

    1) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\container\impl\tomcat

       TomcatBpmPlatformBootstrap.java: deployBpmPlatform() - the last two steps

    2) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\container\impl\deployment\jobexecutor

       MessageProducerStep.java
       MysqlBatchModelRetrievalStep.java

    3) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\engine\impl\cmd

       SubmitStartFormCmd.java: execute()

    4) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\engine\impl\pvm\process

       ActivityImpl.java: blocked variable & getter/setter
       ScopeImpl.java: createActivity() - second if statement

    5) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\engine\impl\pvm\runtime\operation

       PvmAtomicOperationActivityExecute.java: execute() - the first two if statements & MessageProducer in the try statement

    6) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\engine\impl\bpmn\behavior
    
       FlowNodeActivityBehavior.java: leave() - set execution from the Engine Adapter



5) Additionally, the following packages are created specifically for the project:


I) .\camunda-batch\camunda-bpm-platform-dev\engine-rest\engine-rest\src\main\java\org\camunda\bpm\engine\rest\extracts

II) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\engine\adapter

III) .\camunda-batch\camunda-bpm-platform-dev\engine\src\main\java\org\camunda\bpm\engine\messaging




******************************************* GENERAL NOTES *******************************************


1) For further details on how the system works and development notes, please consult the thesis document.

2) For any questions regarding development details you may reach me at 2331992d@gmail.com. I will try to respond as soon as possible.




