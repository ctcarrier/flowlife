package com.flowlife.endpoint

import spray._
import com.weiglewilczek.slf4s.Logging
import com.flowlife.dao.{MeshDao, LocationDao}
import com.flowlife.model.{Mesh, Location}
import httpx.LiftJsonSupport
import routing._
import com.flowlife.spray.MongoAuthSupport

/**
 * @author chris carrier
 */

trait MeshEndpoint extends HttpService with MeshDirectives with LiftJsonSupport with Logging with MongoAuthSupport {

  val meshDao: MeshDao

  def meshRoute =
    path("meshes"){
      authenticate(httpMongo()) {user =>
        post {
          entity(as[Mesh]) {
            save
          }
        } ~
          directGet {
            getMesh
          }
      }
    }

  private def save: Mesh => Route = { adventurer =>
    val res = meshDao.save(adventurer)
    complete(res.toString)
  }

  private def getMesh: String => Route = { key =>
    complete(meshDao.get(key))
  }
}