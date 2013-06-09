package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class User(id: Long, username: String, email: String)

object User {

  val user = {
    get[Long]("id")~
    get[String]("username")~
    get[String]("password") map {
      case id~username~password => User(id, username, password)
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from users where
          email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.user.singleOpt)}}
}
