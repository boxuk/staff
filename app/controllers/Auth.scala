package controllers

import play.api._
import play.api.mvc._

trait Auth extends Controller {
  def Secured[A](username: String, password: String)(action: Action[A]) =
    Action(action.parser) { request =>
      Ok
    }
}
