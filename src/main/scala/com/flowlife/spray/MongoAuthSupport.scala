package com.flowlife.spray

import spray.routing.authentication.BasicHttpAuthenticator
import com.flowlife.auth.FromMongoUserPassAuthenticator
import akka.actor.ActorSystem
import spray.routing.authentication._
import akka.dispatch.ExecutionContext

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 3/12/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
trait MongoAuthSupport {

  implicit def executor: ExecutionContext

  def httpMongo[U](realm: String = "Secured Resource",
                   authenticator: UserPassAuthenticator[U] = FromMongoUserPassAuthenticator())
  : BasicHttpAuthenticator[U] =
    new BasicHttpAuthenticator[U](realm, authenticator)
}
