package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.{ Employee, Role }

import play.api.libs.ws._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._

trait Github {
  def get(url: String) =
    WS.url(url).get().map { response => response.body }

  def profile(username: String) =
    get("https://api.github.com/users/" ++ username)
}

object Employees extends Controller with Github {

  val employeeForm = Form(
    tuple(
      "first_name"   -> nonEmptyText,
      "last_name"    -> nonEmptyText,
      "email"        -> nonEmptyText,
      "phone"        -> nonEmptyText,
      "role"         -> number,
      "website"      -> text,
      "bio"          -> text
    )
  )

  // CRUD

  def index() = Action {
    Ok(views.html.employees.byRole(Employee.groupByRole, Role.all))
  }

  def edit(id: Long) = Action {
    val e: Option[Employee] = Employee.findById(id)
    Ok(views.html.employees.edit(employeeForm, Role.all, e))
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
        BadRequest(views.html.employees.add(errors, Role.all))
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
   * Updates an existing employee record
   *
   */
  def update(id: Long) = Action { implicit request =>
    employeeForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.employees.edit(errors, Role.all, None))
      },
      employee => {
        val e: Employee = Employee.build(employee)
        Employee.update(id, e)
        Redirect(routes.Employees.show(id)).flashing(
          "message" -> "Employee updated"
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

  def withRole(role_id: Long) = Action {
    Ok(views.html.employees.index(Employee.byRole(role_id), Role.all))
  }

  def search = Action { implicit request =>
    val params: Option[String] = request.getQueryString("q")
    params match {
      case None => Ok
      case Some(q) => Ok(views.html.employees.index(Employee.search(q), Role.all))
    }
  }
}

