package com.flowlife.endpoint

import spray.http._
import HttpMethods._
import net.liftweb.json.DefaultFormats
import spray._
import com.weiglewilczek.slf4s.Logging
import com.flowlife.dao.{TrickDao, LocationDao}
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

  val indirectGetTrick = path("") & get & parameter('category ?)

  def trickRoute =
    path("api" / "tricks"){
      post {
        entity(as[Trick]) {
          save
        }
      } ~
      directGet {
        getTrick
      } ~
      indirectGetTrick {
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

  private def getAllTricks: Option[String] => Route = { category =>
    complete(trickDao.getAll(category))
  }
}