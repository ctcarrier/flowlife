package com.flowlife.auth

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 3/12/13
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
import org.bson.types.ObjectId
import com.mongodb.casbah.MongoConnection._
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.weiglewilczek.slf4s.Logging
import scala.util.Properties
import akka.dispatch.{ExecutionContext, Future}
import com.flowlife.mongo.MongoSettings
import spray.routing.authentication._
import com.flowlife.model.Trick
import com.typesafe.config.ConfigFactory

/**
 * @author chris_carrier
 * @version 10/19/11
 */

object FromMongoUserPassAuthenticator extends Logging {
  val config = ConfigFactory.load()

  def apply()(implicit executor: ExecutionContext): UserPassAuthenticator[Trick] = {
    new UserPassAuthenticator[Trick] {
      def apply(userPass: Option[UserPass]) = {
        logger.info("Mongo auth")
        Future {

          userPass.flatMap(up => {
            logger.info("Autenticating: " + up.user + " " + up.pass)
            val MongoSettings(db) = Some(config.getString("mashqwest.db.url"))
            val userColl = db(config.getString("mashqwest.adventurer.collection"))
            val userResult = userColl.findOne(MongoDBObject("email" -> up.user) ++ ("password" -> up.pass))
            userResult.map(grater[Trick].asObject(_))
          })

        }
      }
    }
  }

}