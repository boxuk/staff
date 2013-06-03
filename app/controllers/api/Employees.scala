package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.Employee

object Employees extends Controller {

  private val json = "application/json"

  // GET /api/employees
  def index() = Action {
    val employees: List[Employee] = Employee.all()
    Ok(Json.toJson(employees)).as(json)
  }

  def show(id: Long) = Action {
    val employee: Option[Employee] = Employee.findById(id)
    Ok(Json.toJson(employee)).as(json)
  }

  def delete(id: Long) = Action {
    Ok
  }

  def create() = Action {
    Ok
  }
}

