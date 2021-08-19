package org.dedkot.app

import org.scalatra._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

class MainServlet extends ScalatraServlet {

  private var tables: Seq[String] = Seq.empty

  before("/tables*", cookies.get("username").isEmpty) {
    redirect("/")
  }

  get("/") {
    if (cookies.get("username").isDefined) redirect("/tables")

    views.html.hello()
  }

  post("/username") {
    val username = params("username")
    val ttl = FiniteDuration(7, TimeUnit.DAYS).toSeconds
    response.addCookie(Cookie("username", username)(CookieOptions(maxAge = ttl.toInt)))

    redirect("/tables")
  }

  get("/tables") {
    val username = cookies.get("username").getOrElse("stranger")

    views.html.tables(username, tables)
  }

  post("/tables/new") {
    val table = params("table")
    tables = tables.appended(table)

    redirect(s"/tables/$table")
  }

  get("/tables/:tableName") {
    val table = params("tableName")
    views.html.table(table)
  }

}
