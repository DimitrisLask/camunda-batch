package org.camunda.bpm.engine.messaging;

public enum Queue {

    info,
    block,
    exception;

    public String getName(){
        return this.name();
    }
}
