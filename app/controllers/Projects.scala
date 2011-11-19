package controllers

import play.Logger
import play.api._
import play.api.mvc._
import play.api.data._

import models.Project

object Projects extends Controller {

  val logForm = Form(
    of(
      "name" -> text,
      "log" -> text
    ) 
  )
  
  /* List all project */
  def index() = Action {
    Ok(views.html.project.index(Project.list()))
  }

  /* View one project */
  def view(name: String) = Action {
    Project.findByName(name).map { project => 
      Ok(views.html.project.view(project))
    }.getOrElse(NotFound)
  }

  /* Create a project if not exist (and add log entry) */
  def addLog(name: String) = Action { implicit request =>
    val logEntry = logForm.bindFromRequest.get
    Logger.debug("Receive new log entry: " + logEntry)
    Project.findByName(name).map { project =>
      project.addLog(logEntry._2)
    }.getOrElse {
      val newProject = Project(name)
      newProject.addLog(logEntry._2)
    }
    Ok
  }

  /* List all logs of one project */
  def getLogs(name: String) = Action {
    import sjson.json._
    import DefaultProtocol._
    import JsonSerialization._

    Ok(tojson(Project(name).logs).toString)
  }
}
