package org.camunda.bpm.container.impl.deployment.jobexecutor;

import org.camunda.bpm.container.impl.spi.DeploymentOperation;
import org.camunda.bpm.container.impl.spi.DeploymentOperationStep;
import org.camunda.bpm.engine.messaging.MessageProducer;
import org.camunda.bpm.engine.messaging.Queue;

public class MessageProducerStep extends DeploymentOperationStep {

    public String getName() {
        return "Starting the Message Producer";
    }

    @Override
    public void performOperationStep(DeploymentOperation operationContext) {

        MessageProducer.publish("Camunda Engine Message Producer started", Queue.info);
    }
}
