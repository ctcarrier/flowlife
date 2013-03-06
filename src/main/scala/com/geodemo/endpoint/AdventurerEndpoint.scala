package com.geodemo.endpoint

import spray.http._
import HttpMethods._
import net.liftweb.json.DefaultFormats
import spray._
import com.weiglewilczek.slf4s.Logging
import com.geodemo.dao.{AdventurerDao, LocationDao}
import com.geodemo.json.ObjectIdSerializer
import akka.actor.Actor
import com.geodemo.model.{Adventurer, Location}
import httpx.LiftJsonSupport
import routing._

/**
 * @author chris carrier
 */

trait AdventurerEndpoint extends Actor with HttpService with LiftJsonSupport with Logging {
  implicit val liftJsonFormats = DefaultFormats.lossless + new ObjectIdSerializer

  val dao: AdventurerDao

  implicit def actorRefFactory = context

  def receive = regularRoute

  val anyStringMatch = path("^[a-zA-Z0-9]+$".r)
  val directGet = anyStringMatch & get

  def regularRoute: Receive = runRoute {
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
  }

  def echoComplete[T]: T => Route = { x => complete(x.toString) }

  def save: Adventurer => Route = { adventurer =>
    dao.save(adventurer)
    complete(adventurer.toString)
  }

  def getAdventurer: String => Route = { key =>
    complete(dao.get(key))
  }
}