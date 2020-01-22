/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.pvm.runtime.operation;

import org.camunda.bpm.engine.adapter.BatchEntity;
import org.camunda.bpm.engine.adapter.EngineAdapter;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.interceptor.CommandInvocationContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmException;
import org.camunda.bpm.engine.impl.pvm.PvmLogger;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.runtime.Callback;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.camunda.bpm.engine.impl.util.ClassLoaderUtil;
import org.camunda.bpm.engine.messaging.MessageProducer;
import org.camunda.bpm.engine.messaging.Queue;

import static org.camunda.bpm.engine.impl.util.ActivityBehaviorUtil.getActivityBehavior;

/**
 * @author Tom Baeyens
 */
public class PvmAtomicOperationActivityExecute implements PvmAtomicOperation {

  private final static PvmLogger LOG = PvmLogger.PVM_LOGGER;

  public boolean isAsync(PvmExecutionImpl execution) {
    return false;
  }

  public void execute(PvmExecutionImpl execution) {

    final ActivityImpl activity = execution.getActivity();

    if(activity.isBlock() && execution.isBlock()){
      ProcessEngineConfigurationImpl config = Context.getProcessEngineConfiguration();
      CommandInvocationContext commandInvocationContext = Context.getCommandInvocationContext();
      ClassLoader executionContextClassLoader = Thread.currentThread().getContextClassLoader();
      execution.setContextClassLoader(executionContextClassLoader);
      EngineAdapter.getBatchList().add(new BatchEntity(execution, config, commandInvocationContext));
      String id = execution.getParentId() != null ? execution.getParentId() : execution.getId();
      EngineAdapter.INSTANCE.prepareProcessInstanceForBatch((ExecutionEntity)execution);
      MessageProducer.publish("Activity '"+activity.getName()+"' is blocked from executing... Process Instance id is: "+id, Queue.info);
      return;
    }

    //If this process instance was blocked before, then set the context class loader
    if(execution.wasBlocked()){
      ClassLoaderUtil.setContextClassloader(execution.getContextClassLoader());
    }

    execution.activityInstanceStarted();

    execution.continueIfExecutionDoesNotAffectNextOperation(new Callback<PvmExecutionImpl, Void>() {
      @Override
      public Void callback(PvmExecutionImpl execution) {
        if (execution.getActivity().isScope()) {
          execution.dispatchEvent(null);
        }
        return null;
      }
    }, new Callback<PvmExecutionImpl, Void>() {

      @Override
      public Void callback(PvmExecutionImpl execution) {

        ActivityBehavior activityBehavior = getActivityBehavior(execution);

        ActivityImpl activity = execution.getActivity();
        LOG.debugExecutesActivity(execution, activity, activityBehavior.getClass().getName());

        try {
          MessageProducer.publish("Now executing activity: "+activity.getName(), Queue.info);
          activityBehavior.execute(execution);
        } catch (RuntimeException e) {
          throw e;
        } catch (Exception e) {
          throw new PvmException("couldn't execute activity <" + activity.getProperty("type") + " id=\"" + activity.getId() + "\" ...>: " + e.getMessage(), e);
        }
        return null;
      }
    }, execution);
  }

  public String getCanonicalName() {
    return "activity-execute";
  }

  public boolean isAsyncCapable() {
    return false;
  }
}
