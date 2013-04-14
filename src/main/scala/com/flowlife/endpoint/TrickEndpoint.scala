package com.flowlife.endpoint

import spray.http._
import HttpMethods._
import net.liftweb.json.DefaultFormats
import spray._
import com.weiglewilczek.slf4s.Logging
import com.flowlife.dao.{AdventurerDao, LocationDao}
import com.flowlife.json.ObjectIdSerializer
import akka.actor.Actor
import com.flowlife.model.{Trick, Location}
import httpx.LiftJsonSupport
import routing._

/**
 * @author chris carrier
 */

trait TrickEndpoint extends HttpService with MeshDirectives with LiftJsonSupport with Logging {

  val trickDao: TrickDao

  def adventurerRoute =
    path("tricks"){
      post {
        entity(as[Trick]) {
          save
        }
      } ~
      directGet {
        getTrick
      } ~
      indirectGet {
        getAllTricks
      }
    }

  private def save: Trick => Route = { trick =>
    val res = trickDao.save(trick)
    complete(res.toString)
  }

  private def getTrick: String => Route = { key =>
    complete(trickDao.get(key))
  }

  private def getAllTricks = {
    complete(trickDao.getAll)
  }
}