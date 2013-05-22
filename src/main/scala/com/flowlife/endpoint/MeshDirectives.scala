package com.flowlife.endpoint

import spray.routing._
import com.flowlife.spray.MongoAuthSupport
import spray.routing.directives.RouteDirectives._
import spray.http.StatusCodes._
import spray.http.HttpHeaders.`WWW-Authenticate`
import spray.http.HttpChallenge
import spray.http.HttpChallenge
import spray.routing.AuthenticationRequiredRejection

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 3/11/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
trait MeshDirectives extends Directives with MongoAuthSupport {

  val directGet = path(PathElement) & get

  val indirectGet = path("") & get

  val mongoAuth = authenticate(httpMongo())

  val securePost = post & mongoAuth

  implicit val myRejectionHandler = RejectionHandler.fromPF {
    case AuthenticationRequiredRejection(scheme, realm, params) :: _ =>
      complete(Forbidden, `WWW-Authenticate`(HttpChallenge(scheme, realm, params)) :: Nil,
        "The resource requires authentication, which was not supplied with the request")
    case AuthenticationFailedRejection(realm) :: _ =>
      complete(Forbidden, "The supplied authentication is invalid")
  }
}
