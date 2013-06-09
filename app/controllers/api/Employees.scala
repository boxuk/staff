package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.Employee

object Employees extends Controller {

  private val json = "application/json"

  /**
   * Returns a JSON response listing all employees
   * GET /api/employees
   *
   */
  def index() = Action {
    val employees: List[Employee] = Employee.all()
    Ok(Json.toJson(employees)).as(json)
  }

  /**
   * Returns a JSON repsonse listing information for a single employee
   *
   */
  def show(id: Long) = Action {
    val employee: Option[Employee] = Employee.findById(id)
    Ok(Json.toJson(employee)).as(json)
  }

  /** DELETE /api/employees/:id */
  def delete(id: Long) = Action { implicit request =>
    request.body.asFormUrlEncoded match {
      case Some(params) => {
        val user = params.get("user").getOrElse("")
        Ok(user.toString).as(json)
      }
      case None => BadRequest
    }
  }

  def create() = Action { Ok }

  def update() = Action { Ok }
}

