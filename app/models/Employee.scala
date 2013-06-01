package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

trait PersonalDetails {
  val email: String
}

case class Employee(id:      Long,
                    first:   String,
                    last:    String,
                    email:   String,
                    phone:   String,
                    website: String,
                    bio:     String)

object Employee {

  val employee = {
    get[Long]("id")~
    get[String]("first_name")~
    get[String]("last_name")~
    get[String]("email")~
    get[String]("phone")~
    get[String]("website")~
    get[String]("bio") map {
      case (id ~ first ~ last ~ email ~ phone ~ website ~ bio) => {
        Employee(id, first, last, email, phone, website, bio)
      }
    }
  }

  def all(): List[Employee] = DB.withConnection { implicit c =>
    SQL("""select * from employees""").as(employee *)
  }

  def findById(id: Long): Option[Employee] = {
    None
  }

  def gravatarUrl(email: String): String = {
    ""
  }
}

