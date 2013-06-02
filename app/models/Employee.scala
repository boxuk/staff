package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Employee(
  first:   String,
  last:    String,
  email:   String,
  phone:   String,
  website: String,
  bio:     String )

object Employee {

  val employee = {
    get[String]("first_name")~
    get[String]("last_name")~
    get[String]("email")~
    get[String]("phone")~
    get[String]("website")~
    get[String]("bio") map {
      case (first ~ last ~ email ~ phone ~ website ~ bio) => {
        Employee(first, last, email, phone, website, bio)
      }
    }
  }

  def all(): List[Employee] = DB.withConnection { implicit c =>
    SQL("""select * from employees""").as(employee *)
  }

  def create(employee: Employee): Unit = { }

  def findById(id: Long): Option[Employee] = {
    None
  }

  def gravatarUrl(email: String): String = {
    ""
  }
}

