package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Employees extends Controller {

  val employeeForm = Form(
    tuple(
      "first_name"   -> nonEmptyText,
      "last_name"    -> nonEmptyText,
      "email"        -> nonEmptyText,
      "phone"        -> nonEmptyText,
      "website"      -> nonEmptyText,
      "bio"          -> nonEmptyText
    )
  )

  def index() = Action {
    Ok(views.html.employees.index())
  }

  def add = Action { implicit request =>
    Ok(views.html.employees.add(employeeForm))
  }

  def create = Action { implicit request =>
    employeeForm.bindFromRequest.fold(
      errors => BadRequest(views.html.employees.add(errors)).flashing("error" -> "error"),
      { case (first, last, email, phone, website, bio) => Redirect(routes.Employees.index()) }
    )
  }
}

