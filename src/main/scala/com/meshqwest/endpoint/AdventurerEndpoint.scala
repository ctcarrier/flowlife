package com.meshqwest.endpoint

import spray.http._
import HttpMethods._
import net.liftweb.json.DefaultFormats
import spray._
import com.weiglewilczek.slf4s.Logging
import com.meshqwest.dao.{AdventurerDao, LocationDao}
import com.meshqwest.json.ObjectIdSerializer
import akka.actor.Actor
import com.meshqwest.model.{Adventurer, Location}
import httpx.LiftJsonSupport
import routing._

/**
 * @author chris carrier
 */

trait AdventurerEndpoint extends HttpService with LiftJsonSupport with Logging {

  val adventurerDao: AdventurerDao

  val anyStringMatch = path("^[a-zA-Z0-9]+$".r)
  val directGet = anyStringMatch & get

  def adventurerRoute =
    path("adventurers"){
      post {
        entity(as[Adventurer]) {
          save
        }
      } ~
      directGet {
        getAdventurer
      }
    }

  private def save: Adventurer => Route = { adventurer =>
    adventurerDao.save(adventurer)
    complete(adventurer.toString)
  }

  private def getAdventurer: String => Route = { key =>
    complete(adventurerDao.get(key))
  }
}