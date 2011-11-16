package controllers

import play.api._
import play.api.mvc._

object Logs extends Controller {
  
  /**
   * Create a log entry.
   */
  def create(event: String) = Action {
    println(event)
    Ok(event)
  }
}
