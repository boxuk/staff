package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.Employee

object Employees extends Controller {

  def index() = Action {
    val employees: List[Employee] = Employee.all()
    Ok(Json.toJson(4)).as("application/json")
  }
}

