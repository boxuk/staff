package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.data.validation.Constraints._

import models.{ Role }

object Roles extends Controller {

  val roleForm = Form(
   tuple(
     "role" -> text
   )
  )

  def index() = Action {
    Ok(views.html.roles.index(Role.all()))
  }
}
