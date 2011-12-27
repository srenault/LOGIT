package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Security
import play.api.data._
import format.Formats._
import validation.Constraints._

import models.{User, Project}

object Application extends Controller {

  def currentUser(implicit request: RequestHeader): Option[User] = {
    play.Logger.debug("security" + Security.username)
    session.get(Security.username).map { pseudo =>
      play.Logger.debug("security" + pseudo)
      User.byPseudo(pseudo)
    }.getOrElse(None)
  }

  /**
   * Home page.
   */
  def index() = Action {
    Ok(views.html.index(signupForm))
  }

  /**
   * Create a new user.
   */
  def signup() = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      errors => Ok(views.html.index(signupForm)),
      {
        case u: User => 
          User.create(u)
          Redirect(routes.Application.index())
      })
  }

  
  val signupForm = Form(
    of(User.apply _, User.unapply _) (
      "pseudo"   -> of[String].verifying(required),
      "email"    -> of[String].verifying(required),
      "password" -> of[String].verifying(required)
    )
  )

  /**
   * Authenticate user.
   */
  def signin() = Action { implicit request =>
    signinForm.bindFromRequest.fold(
      errors => Ok(views.html.index(signupForm)),
      {
        case (pseudo, password) => 
          User.authenticate(pseudo, password).map { u =>
            play.Logger.debug("Authentication successful. Redirecting to user home page...")
            Redirect(routes.Users.index()).withSession(session + (Security.username -> u.pseudo))
          }.getOrElse{
            Redirect(routes.Application.index())
          }
      })
  }

  val signinForm = Form(
    of(
      "pseudo" -> text,
      "password" -> text
    ) 
  )

  /**
   * Log Out user.
   */
  def logout() = Action {
    Ok
  }
}

