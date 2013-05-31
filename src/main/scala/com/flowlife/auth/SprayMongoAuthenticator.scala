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
import com.flowlife.model.{User, Trick}
import com.typesafe.config.ConfigFactory

/**
 * @author chris_carrier
 * @version 10/19/11
 */

object FromMongoUserPassAuthenticator extends Logging {
  val config = ConfigFactory.load()

  def apply()(implicit executor: ExecutionContext): UserPassAuthenticator[User] = {
    new UserPassAuthenticator[User] {
      def apply(userPass: Option[UserPass]) = {
        logger.info("Mongo auth")
        Future {

          userPass.flatMap(up => {
            logger.info("Autenticating: " + up.user + " " + up.pass)
            val mongoUrl = config.getString("flowlife.db.url")
            val MongoSettings(db) = Some(Properties.envOrElse("MONGOHQ_URL", mongoUrl))
            val userColl = db(config.getString("flowlife.user.collection"))
            val userResult = userColl.findOne(MongoDBObject("email" -> up.user) ++ ("password" -> up.pass))
            userResult.map(grater[User].asObject(_))
          })

        }
      }
    }
  }

}
