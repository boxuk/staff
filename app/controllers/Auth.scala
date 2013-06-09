package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import models.{ User }

trait Secured {

}

trait Auth extends Controller with Secured {

  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text ) verifying (
        "Invalid email or password", result => {
          result match {
            case (e,p) => User.authenticate(e,p).isDefined
          }
      }))

  def login() = Action { implicit request =>
    Ok(views.html.auth.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.auth.login(formWithErrors))
      },
      user => {
        Redirect(routes.Application.index)
          .withSession(Security.username -> user._1)
      })
  }
}
