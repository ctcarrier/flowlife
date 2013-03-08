package com.geodemo.endpoint

import akka.actor.Actor
import spray.routing.HttpService
import net.liftweb.json.DefaultFormats
import com.geodemo.json.ObjectIdSerializer

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 3/8/13
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
trait MasterEndpoint extends Actor with AdventurerEndpoint with LocationEndpoint with HttpService {

  implicit def actorRefFactory = context

  implicit val liftJsonFormats = DefaultFormats.lossless + new ObjectIdSerializer

  def receive = runRoute(locationRoute ~ adventurerRoute)

}
