package controllers

import play.api._
import play.api.mvc._

import models.{ Employee }

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(Employee.recent))
  }

}
