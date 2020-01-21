// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Vortex/Desktop/Dev/batchcontroller/conf/routes
// @DATE:Wed Nov 20 15:08:05 CET 2019

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:8
  class ReverseProcessController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def sendList(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "get-blocked-processes")
    }
  
    // @LINE:16
    def resume(id:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "resume/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
  }

  // @LINE:6
  class ReverseModelController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:14
    def updateModel(json:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "update/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("json", json)))
    }
  
    // @LINE:10
    def createModel(json:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "create/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("json", json)))
    }
  
    // @LINE:12
    def deleteModel(id:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "delete/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:6
    def getModels(): Call = {
      
      Call("GET", _prefix)
    }
  
  }


}
