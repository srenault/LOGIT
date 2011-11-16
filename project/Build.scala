import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "logIt"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "com.mongodb.casbah" % "casbah_2.9.0-1" % "2.1.5.0",
      "net.debasishg" % "sjson_2.9.1" % "0.15"
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      // Add your own project settings here      
    )

}
