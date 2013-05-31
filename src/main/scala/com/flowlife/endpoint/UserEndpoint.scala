package com.flowlife.endpoint

import spray._
import com.weiglewilczek.slf4s.Logging
import com.flowlife.dao.{UserDao, TrickDao}
import com.flowlife.model.Trick
import httpx.LiftJsonSupport
import routing._

/**
 * @author chris carrier
 */

trait UserEndpoint extends HttpService with MeshDirectives with LiftJsonSupport with Logging {

  //val userDao: UserDao

  def userRoute =
    pathPrefix("api" / "users"){
      mongoAuth { user =>
        get {
          complete(user)
        }
      }
    }
}