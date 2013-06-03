package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.Employee

object Employees extends Controller {

  val employeeForm = Form(
    tuple(
      "first_name"   -> nonEmptyText,
      "last_name"    -> nonEmptyText,
      "email"        -> nonEmptyText,
      "phone"        -> nonEmptyText,
      "website"      -> text,
      "bio"          -> text
    )
  )

  // CRUD

  def index() = Action {
    Ok(views.html.employees.index(Employee.all))
  }

  def show(id: Long) = Action {
    val e: Option[Employee] = Employee.findById(id)
    Ok(views.html.employees.show(e))
  }

  def add = Action { implicit request =>
    Ok(views.html.employees.add(employeeForm))
  }

  def create = Action { implicit request =>
    employeeForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.employees.add(errors))
      },
      employee => {
        val e: Employee = Employee.build(employee)
        Employee.create(e)
        Redirect(routes.Employees.index).flashing(
          "message" -> "A new employee was created"
        )
      }
    )
  }
}

