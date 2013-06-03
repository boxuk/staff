package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.{ Role }

object Roles extends Controller {

  private val json = "application/json"

  def index() = Action {
    val roles: List[Role] = Role.all()
    Ok(Json.toJson(roles)).as(json)
  }
}
