// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Vortex/Desktop/Dev/batchcontroller/conf/routes
// @DATE:Wed Nov 20 15:08:05 CET 2019

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {

  // @LINE:8
  class ReverseProcessController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def sendList: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProcessController.sendList",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "get-blocked-processes"})
        }
      """
    )
  
    // @LINE:16
    def resume: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ProcessController.resume",
      """
        function(id0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "resume/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseModelController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:14
    def updateModel: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ModelController.updateModel",
      """
        function(json0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "update/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("json", json0))})
        }
      """
    )
  
    // @LINE:10
    def createModel: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ModelController.createModel",
      """
        function(json0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "create/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("json", json0))})
        }
      """
    )
  
    // @LINE:12
    def deleteModel: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ModelController.deleteModel",
      """
        function(id0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "delete/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
    // @LINE:6
    def getModels: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ModelController.getModels",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
  }


}
