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

trait WebappEndpoint extends HttpService with LiftJsonSupport with Logging {

  def webappRoute =
      pathPrefix("app"){
          get {
            getFromResourceDirectory("webapp")
          }

      }

}