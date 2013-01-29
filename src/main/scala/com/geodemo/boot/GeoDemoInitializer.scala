package com.geodemo.boot

import util.Properties
import akka.actor.{ActorSystem, Props}
import com.weiglewilczek.slf4s.Logging
import com.typesafe.config.ConfigFactory
import com.geodemo.mongo.MongoSettings
import com.geodemo.endpoint.LocationEndpoint
import com.geodemo.dao.{MongoLocationDao, LocationDao}
import spray.can.server.{HttpServer, SprayCanHttpServerApp}
import spray.io.IOExtension

/**
 * @author chris_carrier
 */

object GeoDemoInitializer extends App with SprayCanHttpServerApp with Logging {

  logger.info("Running Initializer")

  //override lazy val system = ActorSystem("mashqwest")

  val config = ConfigFactory.load()

  val host = "0.0.0.0"
  val port = Option(System.getenv("PORT")).getOrElse("8080").toInt

  val mongoUrl = config.getString("mongodb.url")
  val mongoDbName = config.getString("mongodb.database")

//  val urlList = mongoUrl.split(",").toList.map(new ServerAddress(_))

  val MongoSettings(db) = Properties.envOrNone("MONGOHQ_URL")

  val locationCollection = db(config.getString("geodemo.location.collection"))

  val locationDao = new MongoLocationDao(locationCollection)
  // ///////////// INDEXES for collections go here (include all lookup fields)
  //  configsCollection.ensureIndex(MongoDBObject("customerId" -> 1), "idx_customerId")

  val locationHandler = system.actorOf(
    Props(new LocationEndpoint {
      val dao = locationDao
    }),
    name = "location-service"
  )

  // every spray-can HttpServer (and HttpClient) needs an IoWorker for low-level network IO
  // (but several servers and/or clients can share one)
  //override val ioBridge = IOExtension(system).ioBridge

  newHttpServer(locationHandler) ! Bind(interface = "localhost", port = 8080)

}

