package com.flowlife.endpoint

import spray._
import com.weiglewilczek.slf4s.Logging
import com.flowlife.dao.{TrickCategoryDao, TrickDao}
import com.flowlife.model.{TrickCategory, Trick}
import httpx.LiftJsonSupport
import routing._

/**
 * @author chris carrier
 */

trait TrickCategoryEndpoint extends HttpService with MeshDirectives with LiftJsonSupport with Logging {

  val trickCategoryDao: TrickCategoryDao

  def trickCategoryRoute =
    path("api" / "trickCategories"){
      post {
        entity(as[TrickCategory]) {
          save
        }
      } ~
      directGet {
        getTrickCategory
      } ~
      indirectGet {
        getAllTrickCategories
      }
    }

  private def save: TrickCategory => Route = { trickCategory =>
    val res = trickCategoryDao.save(trickCategory)
    complete(res)
  }

  private def getTrickCategory: String => Route = { key =>
    complete(trickCategoryDao.get(key))
  }

  private def getAllTrickCategories = {
    complete(trickCategoryDao.getAll)
  }
}