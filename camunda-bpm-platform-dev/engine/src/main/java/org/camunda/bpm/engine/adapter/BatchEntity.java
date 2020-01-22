package org.camunda.bpm.engine.adapter;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandInvocationContext;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.camunda.bpm.engine.messaging.MessageProducer;
import org.camunda.bpm.engine.messaging.Queue;
import org.springframework.util.ClassUtils;

import java.util.Map;

public class BatchEntity {

    private PvmExecutionImpl execution;
    private ProcessEngineConfigurationImpl config;
    private CommandInvocationContext commandInvocationContext;

    public BatchEntity(PvmExecutionImpl execution, ProcessEngineConfigurationImpl config,
                       CommandInvocationContext commandInvocationContext){

        this.execution = execution;
        this.config = config;
        this.commandInvocationContext = commandInvocationContext;
    }

    public String getId(){
        return this.execution.getId();
    }

    public String getParentId(){
        return this.execution.getParentId();
    }

    public PvmExecutionImpl getExecution(){
        return this.execution;
    }

    public ProcessEngineConfigurationImpl getProcessEngineConfiguration(){
        return this.config;
    }

    public CommandInvocationContext getCommandInvocationContext(){
        return this.commandInvocationContext;
    }

    protected void compareVariables(Map<String, Object> newVariables){

        Map<String, Object> currentVariables = execution.getVariables();
        Object o = null;
        for(String str : currentVariables.keySet()){
            Class<?> clazz = currentVariables.get(str).getClass();
            Class<?> newClazz = newVariables.get(str).getClass();
            if(clazz == Long.class && newClazz == Integer.class){
                o = newVariables.get(str);
            }
            try{
                o = (long) (int) o;
                newVariables.put(str, o);
            }
            catch(Exception e){
                MessageProducer.publish(this.getClass().getName()+": "+e.toString(), Queue.exception);
            }
        }

    }

}
