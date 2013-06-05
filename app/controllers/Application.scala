package controllers

import play.api._
import play.api.mvc._

import models.{ Employee }

trait Debug {

  def getHeader(request: Request[AnyContent], header: String): String =
    request.headers.get(header).getOrElse("Not found")

  def headers(request: Request[AnyContent]): String =
    request.headers.keys.mkString(" ")
}

object Application extends Controller with Debug {

  def index = Action {
    Ok(views.html.index(Employee.recent))
  }

  // Action for debugging HTTP requests
  def header = Action { request =>
    Ok(getHeader(request, "Cookie"))
  }
}
