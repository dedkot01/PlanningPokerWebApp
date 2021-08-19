package org.dedkot.app

import org.scalatra._

class MainServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

  get("/tables") {
    val username = cookies.get("username").getOrElse("stranger")
    views.html.tables(username)
  }

  post("/username") {
    val username = params("username")
    response.addCookie(Cookie("username", username)(CookieOptions(maxAge = 7 * 24 * 60 * 60)))
    views.html.user(username)
  }

}
