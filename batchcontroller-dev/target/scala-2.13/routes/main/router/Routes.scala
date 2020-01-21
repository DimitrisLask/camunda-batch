// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Vortex/Desktop/Dev/batchcontroller/conf/routes
// @DATE:Wed Nov 20 15:08:05 CET 2019

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  ModelController_1: controllers.ModelController,
  // @LINE:8
  ProcessController_0: controllers.ProcessController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    ModelController_1: controllers.ModelController,
    // @LINE:8
    ProcessController_0: controllers.ProcessController
  ) = this(errorHandler, ModelController_1, ProcessController_0, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, ModelController_1, ProcessController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.ModelController.getModels"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """get-blocked-processes""", """controllers.ProcessController.sendList"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """create/""" + "$" + """json<[^/]+>""", """controllers.ModelController.createModel(json:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """delete/""" + "$" + """id<[^/]+>""", """controllers.ModelController.deleteModel(id:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """update/""" + "$" + """json<[^/]+>""", """controllers.ModelController.updateModel(json:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """resume/""" + "$" + """id<[^/]+>""", """controllers.ProcessController.resume(id:String)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_ModelController_getModels0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_ModelController_getModels0_invoker = createInvoker(
    ModelController_1.getModels,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ModelController",
      "getModels",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val controllers_ProcessController_sendList1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("get-blocked-processes")))
  )
  private[this] lazy val controllers_ProcessController_sendList1_invoker = createInvoker(
    ProcessController_0.sendList,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProcessController",
      "sendList",
      Nil,
      "GET",
      this.prefix + """get-blocked-processes""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_ModelController_createModel2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("create/"), DynamicPart("json", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ModelController_createModel2_invoker = createInvoker(
    ModelController_1.createModel(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ModelController",
      "createModel",
      Seq(classOf[String]),
      "POST",
      this.prefix + """create/""" + "$" + """json<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val controllers_ModelController_deleteModel3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("delete/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ModelController_deleteModel3_invoker = createInvoker(
    ModelController_1.deleteModel(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ModelController",
      "deleteModel",
      Seq(classOf[String]),
      "POST",
      this.prefix + """delete/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_ModelController_updateModel4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("update/"), DynamicPart("json", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ModelController_updateModel4_invoker = createInvoker(
    ModelController_1.updateModel(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ModelController",
      "updateModel",
      Seq(classOf[String]),
      "POST",
      this.prefix + """update/""" + "$" + """json<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_ProcessController_resume5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("resume/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ProcessController_resume5_invoker = createInvoker(
    ProcessController_0.resume(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ProcessController",
      "resume",
      Seq(classOf[String]),
      "POST",
      this.prefix + """resume/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_ModelController_getModels0_route(params@_) =>
      call { 
        controllers_ModelController_getModels0_invoker.call(ModelController_1.getModels)
      }
  
    // @LINE:8
    case controllers_ProcessController_sendList1_route(params@_) =>
      call { 
        controllers_ProcessController_sendList1_invoker.call(ProcessController_0.sendList)
      }
  
    // @LINE:10
    case controllers_ModelController_createModel2_route(params@_) =>
      call(params.fromPath[String]("json", None)) { (json) =>
        controllers_ModelController_createModel2_invoker.call(ModelController_1.createModel(json))
      }
  
    // @LINE:12
    case controllers_ModelController_deleteModel3_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ModelController_deleteModel3_invoker.call(ModelController_1.deleteModel(id))
      }
  
    // @LINE:14
    case controllers_ModelController_updateModel4_route(params@_) =>
      call(params.fromPath[String]("json", None)) { (json) =>
        controllers_ModelController_updateModel4_invoker.call(ModelController_1.updateModel(json))
      }
  
    // @LINE:16
    case controllers_ProcessController_resume5_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ProcessController_resume5_invoker.call(ProcessController_0.resume(id))
      }
  }
}
