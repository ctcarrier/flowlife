package com.meshqwest.endpoint

import spray._
import com.weiglewilczek.slf4s.Logging
import com.meshqwest.dao.LocationDao
import com.meshqwest.model.Location
import httpx.LiftJsonSupport
import routing._

/**
 * @author chris carrier
 */

trait MeshEndpoint extends HttpService with LiftJsonSupport with Logging {

  val locationDao: LocationDao

  def locationRoute =
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


  def echoComplete[T]: T => Route = { x => complete(x.toString) }

  private def saveLocation: Location => Route = { loc =>
    locationDao.saveLocation(loc)
    complete(loc.toString)
  }

  private def getNearbyLocations: (Double, Double) => Route = {(lat, long) =>
    val res = locationDao.getNearbyLocations(5, lat, long)
    complete(res)
  }
}