package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

import play.api.libs.functional.syntax._
import play.api.libs.json._

import lib.{ Gravatar }

case class Employee(
  id:      Option[Long]=None,
  first:   String,
  last:    String,
  email:   String,
  phone:   String,
  role:    Int,
  website: String,
  bio:     String)

object Employee {

  val employee = {
    get[Long]("id")~
    get[String]("first_name")~
    get[String]("last_name")~
    get[String]("email")~
    get[String]("phone")~
    get[Int]("role_id")~
    get[String]("website")~
    get[String]("bio") map {
      case (id~first~last~email~phone~role~website~bio) => {
        Employee(Some(id), first, last, email, phone, role, website, bio)
      }
    }
  }

  def gravatar(email: String, size: Int) =
    new Gravatar(email).url(size)

  /** Build Employee without ID probably a much better way to do this
   *  using tupled and apply */
  def build(e: (String,String,String,String,Int, String,String)): Employee = {
    Employee(None, e._1, e._2, e._3, e._4, e._5, e._6, e._7)
  }

  def name(e: Employee): String = e.first + " " + e.last

  def all(): List[Employee] = DB.withConnection { implicit c =>
    SQL("""select * from employees""").as(employee *)
  }

  def recent(): List[Employee] = DB.withConnection { implicit c =>
    SQL("""select * from employees
           order by id desc
           limit 5"""
    ).as(employee *)
  }

  def findById(id: Long): Option[Employee] = DB.withConnection { implicit c =>
    SQL("select * from employees where id = {id}").on('id -> id)
                                                  .as(employee.singleOpt)
  }

  def create(employee: Employee): Unit = {
    DB.withConnection { implicit c =>
      SQL(
        """insert into employees (first_name, last_name, email, phone, role_id, website, bio)
           values ({first}, {last}, {email}, {phone}, {role}, {website}, {bio})"""
      ).on(
        'first   -> employee.first,
        'last    -> employee.last,
        'email   -> employee.email,
        'phone   -> employee.phone,
        'role    -> employee.role,
        'website -> employee.website,
        'bio     -> employee.bio
      ).executeUpdate()
    }
  }

  def delete(id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("delete from employees where id = {id}").on('id -> id).executeUpdate()
    }
  }

  // JSON serialization
  implicit val employeeFormat = Json.writes[Employee]
  implicit val employeeReads  = Json.reads[Employee]
}

