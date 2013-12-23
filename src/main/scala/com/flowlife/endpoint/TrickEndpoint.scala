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
    pathPrefix("api" / "tricks"){
      securePost { user =>
        entity(as[Trick]) {
          save
        }
      } ~
      path(PathElement) { key =>
        get { ctx =>
          ctx.complete(getTrick(key))
        } ~
        post { ctx =>
          logger.info("In post endpoint")
          entity(as[Trick]) {
            update(key)
          }
        } ~
        delete {
          respondWithStatus(StatusCodes.NoContent) { ctx =>
            logger.info("In delete endpoint")
            deleteTrick(key)
            ctx.complete("")
          }
        }
      } ~
      indirectGetTrick {
        getAllTricks
      }
    }

  private def save: Trick => Route = { trick =>
    val res = trickDao.save(trick)
    complete(res)
  }

  private def update(key: String): Trick => Route = { trick =>
    val res = trickDao.update(key, trick)
    complete(res)
  }

  private def getTrick(key: String) = {
    logger.info("GETTING TRICK")
    trickDao.get(key)
  }

  private def getAllTricks: Option[String] => Route = { category =>
    logger.info("GETTING ALL TRICKS")
    complete(trickDao.getAll(category))
  }

  private def deleteTrick(key: String) = {
    trickDao.delete(key)
  }
}