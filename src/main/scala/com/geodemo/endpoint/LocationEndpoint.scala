package com.geodemo.endpoint

import spray.http._
import MediaTypes._
import HttpMethods._
import net.liftweb.json.DefaultFormats
import spray._
import com.weiglewilczek.slf4s.Logging
import com.geodemo.dao.LocationDao
import com.geodemo.service.LocationService
import com.geodemo.json.{ObjectIdSerializer}
import akka.actor.Actor
import com.geodemo.model.Location
import httpx.LiftJsonSupport
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import routing._

/**
 * @author chris carrier
 */

trait LocationEndpoint extends Actor with HttpService with LiftJsonSupport with Logging {
  implicit val liftJsonFormats = DefaultFormats.lossless + new ObjectIdSerializer

  val dao: LocationDao

  implicit def actorRefFactory = context

  def receive = rawRoute orElse regularRoute

  def rawRoute: Receive = {
    case HttpRequest(GET, "/ping", _, _, _) =>
      sender ! HttpResponse(entity = "PONG!")
  }

  def regularRoute: Receive = runRoute {
    path("locations"){
      post {
        entity(as[Location]) {
          saveLocation
        }
      }
    } ~
    path("nearbyLocations"){
      get {
        parameters("lat".as[Int], "long".as[Int]) {
          getNearbyLocations
        }
      }
    }
  }

  def echoComplete[T]: T => Route = { x => complete(x.toString) }

  def saveLocation: Location => Route = { loc =>
    dao.saveLocation(loc)
    complete(loc.toString)
  }

  def getNearbyLocations: (Int, Int) => Route = {(lat, long) =>
    val res = dao.getNearbyLocations(5, lat, long)
    complete(res)
  }
}