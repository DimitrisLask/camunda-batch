// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Vortex/Desktop/Dev/batchcontroller/conf/routes
// @DATE:Wed Nov 20 15:08:05 CET 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
