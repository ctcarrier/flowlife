package com.flowlife.endpoint

import spray.http._
import MediaTypes._
import HttpMethods._
import net.liftweb.json.DefaultFormats
import spray._
import com.weiglewilczek.slf4s.Logging
import com.flowlife.dao.LocationDao
import com.flowlife.service.LocationService
import com.flowlife.json.{ObjectIdSerializer}
import akka.actor.Actor
import com.flowlife.model.Location
import httpx.LiftJsonSupport
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import routing._

/**
 * @author chris carrier
 */

trait LocationEndpoint extends HttpService with LiftJsonSupport with Logging {

  val locationDao: LocationDao

  def locationRoute =
    respondWithMediaType(`application/json`) {
      path("locations"){
        post {
          entity(as[Location]) {
            saveLocation
          }
        }
      } ~
      path("nearbyLocations"){
        get {
          parameters("lat".as[Double], "long".as[Double]) {
            getNearbyLocations
          }
        }
      }
    }


  def echoComplete[T]: T => Route = { x => complete(x.toString) }

  private def saveLocation: Location => Route = { loc =>
    val res = locationDao.saveLocation(loc)
    complete(res)
  }

  private def getNearbyLocations: (Double, Double) => Route = {(lat, long) =>
    val res = locationDao.getNearbyLocations(5, lat, long)
    complete(res)
  }
}