package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.Employee

import play.api.libs.ws._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._

trait Github {

  def get(url: String) = {
    WS.url(url).get().map { response => response.body }
  }

  def profile(username: String) = {
    get("https://api.github.com/users/" ++ username)
  }
}

object Employees extends Controller with Github {

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

  /**
   * Fetch a users public Github info if they have a profile
   *
   */
  def githubInfo(username: String) = Action {
    Async {
      for {
        response <- profile(username)
      } yield { Ok(response) }
    }
  }

  /**
   * Show an employee profile
   *
   */
  def show(id: Long) = Action {
    val e: Option[Employee] = Employee.findById(id)
    Ok(views.html.employees.show(e))
  }

  /**
   * Add a new employee record
   *
   */
  def add = Action { implicit request =>
    Ok(views.html.employees.add(employeeForm, models.Role.all))
  }

  /**
   * Create a new employee
   *
   */
  def create = Action { implicit request =>
    employeeForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.employees.add(errors, models.Role.all))
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

  /**
   * Deletes an employee by ID
   *
   */
  def delete(id: Long) = Action {
    Employee.delete(id)
    Redirect(routes.Employees.index)
  }
}

