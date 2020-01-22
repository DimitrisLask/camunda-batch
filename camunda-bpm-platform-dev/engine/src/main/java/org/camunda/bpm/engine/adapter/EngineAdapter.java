package org.camunda.bpm.engine.adapter;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.context.ProcessEngineContextImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandInvocationContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.camunda.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.camunda.bpm.engine.messaging.JsonBuilder;
import org.camunda.bpm.engine.messaging.MessageProducer;
import org.camunda.bpm.engine.messaging.Queue;

import java.util.*;

/**
* @author Dimitrios Laskaratos, MSc.
*/

public class EngineAdapter {

    private static Set<String> idList;
    public static final EngineAdapter INSTANCE = new EngineAdapter();
    private static List<BatchEntity> batchList;
    private static Map<String, ExecutionEntity> executionList;
    private static ProcessEngineConfigurationImpl configuration;

    private EngineAdapter(){

        idList = new HashSet<>();
        batchList = new ArrayList<>();
        executionList = new HashMap<>();
    }

    public static void addBatchActivity(String activityId){
        idList.add(activityId);
    }

    public static void addExecution(ExecutionEntity execution){
        executionList.put(execution.getProcessInstanceId(), execution);
    }

    public static void removeBatchActivity(String activityId){
        idList.remove(activityId);
    }

    public boolean findBatchActivityById(String id){
        return idList.contains(id);
    }

    public void resumeProcessInstanceById(String processInstanceId, Map<String, Object> variables, PvmAtomicOperation operation){

        BatchEntity entity = getBatchEntity(processInstanceId);
        PvmExecutionImpl execution = getExecution(entity);

        ProcessEngineConfigurationImpl config = getProcessEngineConfiguration(entity);
        Context.setProcessEngineConfiguration(config);
        Context.setCommandContext(new CommandContext(config));
        Context.setCommandInvocationContext(getCommandInvocationContext(entity));

        if(variables!= null && !variables.isEmpty()){
            entity.compareVariables(variables);
            if(execution.getProcessInstance() != null){
                execution.getProcessInstance().setVariables(variables);
            }
            else{
                execution.setVariables(variables);
            }
        }

        execution.getProcessInstance().setBlock(false);
        execution.getProcessInstance().setWasBlocked(true);

        try{
            MessageProducer.publish("Resuming process instance with id: "+ execution.getProcessInstance().getId(), Queue.info);
            operation.execute(execution);
        }
        catch(Exception e){
            MessageProducer.publish(this.getClass().getName()+": "+e.getMessage(), Queue.exception);
        }
        finally{
            Context.removeProcessEngineConfiguration();
            Context.removeCommandContext();
            Context.removeCommandInvocationContext();
            ProcessEngineContextImpl.set(false);
            batchList.remove(entity);
            executionList.remove(execution.getProcessInstanceId());
        }
    }

    public void resumeProcessInstancesByList(Map<String, Map<String, Object>> processList, PvmAtomicOperation operation){

        for(String processInstanceId : processList.keySet()){
            Map<String, Object> variables = processList.get(processInstanceId);
            resumeProcessInstanceById(processInstanceId, variables, operation);
        }

    }

    public void prepareProcessInstanceForBatch(ExecutionEntity processInstance){

        Map<String, Object> variables;

        if(processInstance.getProcessInstance() != null){
            variables = processInstance.getProcessInstance().getVariables().asValueMap();
        }
        else{
            variables = processInstance.getVariables().asValueMap();
        }

        BatchInstance instance = new BatchInstance(processInstance.getId(),
                processInstance.getParentId(),
                processInstance.getProcessDefinitionId(),
                processInstance.getProcessDefinition().getName(),
                processInstance.getCurrentActivityId(),
                processInstance.getCurrentActivityName(),
                variables);
        MessageProducer.publish(JsonBuilder.toJson(instance), Queue.block);
    }

    public static Set<String> getList(){
        return idList;
    }

    public void setList(Set<String> list){
        idList = list;
    }

    public static List<BatchEntity> getBatchList(){
        return batchList;
    }

    public static Map<String, ExecutionEntity> getExecutionList(){
        return executionList;
    }

    private BatchEntity getBatchEntity(final String processInstanceId){

        BatchEntity entity = null;
        try{
            for(BatchEntity item : batchList){
                if(item.getId().equals(processInstanceId) || (item.getParentId() != null && item.getParentId().equals(processInstanceId))){
                    entity = item;
                    break;
                }

            }

            batchList.remove(entity);
        }
        catch(Exception e){
            MessageProducer.publish(this.getClass().getName()+":"+e.toString(), Queue.info);
        }
        return entity;
    }

    private ProcessEngineConfigurationImpl getProcessEngineConfiguration(BatchEntity entity){
        return entity.getProcessEngineConfiguration();
    }

    private CommandInvocationContext getCommandInvocationContext(BatchEntity entity){
        return entity.getCommandInvocationContext();
    }

    private PvmExecutionImpl getExecution(BatchEntity entity){
        return entity.getExecution();
    }

    public static ProcessEngineConfigurationImpl getConfiguration(){
        return configuration;
    }

    public void setConfiguration(ProcessEngineConfigurationImpl config){
        configuration = config;
    }

}
