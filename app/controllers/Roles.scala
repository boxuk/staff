package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.data.validation.Constraints._

import models.{ Role }

object Roles extends Controller {

  val roleForm = Form(
    "role" -> text
  )

  def index() = Action {
    Ok(views.html.roles.index(Role.all()))
  }

  def add() = Action {
    Ok(views.html.roles.add(roleForm))
  }

  def create = Action { implicit request =>
    roleForm.bindFromRequest.fold(
      errors => {
        println(errors)
        BadRequest(views.html.roles.add(errors))
      },
      role => {
        Role.create(role)
        Redirect(routes.Roles.index).flashing(
          "message" -> "A new role was created"
        )
      }
    )
  }
}
