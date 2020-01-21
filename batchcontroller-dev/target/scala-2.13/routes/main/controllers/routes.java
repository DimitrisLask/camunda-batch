// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Vortex/Desktop/Dev/batchcontroller/conf/routes
// @DATE:Wed Nov 20 15:08:05 CET 2019

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseProcessController ProcessController = new controllers.ReverseProcessController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseModelController ModelController = new controllers.ReverseModelController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseProcessController ProcessController = new controllers.javascript.ReverseProcessController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseModelController ModelController = new controllers.javascript.ReverseModelController(RoutesPrefix.byNamePrefix());
  }

}
