package com.flowlife.endpoint

import akka.actor.Actor
import spray.routing._
import net.liftweb.json.DefaultFormats
import com.flowlife.json.ObjectIdSerializer
import spray.routing.directives.RouteDirectives._
import spray.http.StatusCodes._
import spray.http.HttpHeaders.`WWW-Authenticate`
import spray.http.HttpChallenge
import spray.routing.AuthenticationRequiredRejection
import spray.http.HttpChallenge

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 3/8/13
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
trait MasterEndpoint extends Actor with TrickEndpoint
    with WebappEndpoint
    with TrickCategoryEndpoint
    with UserEndpoint
    with HttpService {

  implicit def actorRefFactory = context
  implicit def executor = context.dispatcher

  implicit val liftJsonFormats = DefaultFormats.lossless + new ObjectIdSerializer

  def masterRoute: Route = trickRoute ~ webappRoute ~ trickCategoryRoute ~ userRoute

  def receive = runRoute(masterRoute)

}
