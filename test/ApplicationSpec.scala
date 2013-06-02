package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {

  "Application" should {
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }
    "render the index page" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/")).get
        status(page) must equalTo(OK)
        //contentAsString(home) must contain ("")
      }
    }
    "render the employee listing page" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/employees")).get
        status(page) must equalTo(OK)
      }
    }
    "render the new employee page" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/employees/new")).get
        status(page) must equalTo(OK)
      }
    }
  }
}
