package org.dedkot.app

import org.scalatra._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

class MainServlet extends ScalatraServlet {

  before("/tables*", cookies.get("username").isEmpty) {
    redirect("/")
  }

  get("/") {
    if (cookies.get("username").isDefined) redirect("/tables")
    views.html.hello()
  }

  get("/tables") {
    val username = cookies.get("username").getOrElse("stranger")
    views.html.tables(username)
  }

  post("/username") {
    val username = params("username")
    val ttl = FiniteDuration(7, TimeUnit.DAYS).toSeconds
    response.addCookie(Cookie("username", username)(CookieOptions(maxAge = ttl.toInt)))
    redirect("/tables")
  }

}
