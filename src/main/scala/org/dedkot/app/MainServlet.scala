package org.dedkot.app

import org.json4s.JsonDSL._
import org.json4s._
import org.scalatra._
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}

import java.util.Date
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext.Implicits.global

class MainServlet extends ScalatraServlet with JValueResult with JacksonJsonSupport with AtmosphereSupport {

  implicit protected val jsonFormats: Formats = DefaultFormats

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

  atmosphere("/tables") {
    new AtmosphereClient {
      def receive: AtmoReceive = {
        case Connected =>
          println("Client %s is connected" format uuid)
          //broadcast(("author" -> "Someone") ~ ("message" -> "joined the room") ~ ("time" -> new Date().getTime.toString), Everyone)

        case Disconnected(ClientDisconnected, _) =>
          println("Client %s is disconnected" format uuid)
          //broadcast(("author" -> "Someone") ~ ("message" -> "has left the room") ~ ("time" -> new Date().getTime.toString), Everyone)

        case Disconnected(ServerDisconnected, _) =>
          println("Server disconnected the client %s" format uuid)

        case _: TextMessage =>
          send(("author" -> "system") ~ ("message" -> "Only json is allowed") ~ ("time" -> new Date().getTime.toString))

        case JsonMessage(json) =>
          println("Got message %s from %s".format((json \ "message").extract[String], (json \ "author").extract[String]))
          val msg = json merge ("time" -> new Date().getTime.toString: JValue)
          broadcast(msg) // by default a broadcast is to everyone but self
        //  send(msg) // also send to the sender
      }
    }
  }

  post("/tables/new") {
    val table = params("table")
    tables = tables :+ table

    redirect(s"/tables/$table")
  }

  get("/tables/:tableName") {
    val table = params("tableName")

    if (!tables.contains(table)) response.setStatus(404)
    else views.html.table(table)
  }

}
