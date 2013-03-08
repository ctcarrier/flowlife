package com.geodemo.boot

import util.Properties
import akka.actor.{ActorSystem, Props}
import com.weiglewilczek.slf4s.Logging
import com.typesafe.config.ConfigFactory
import com.geodemo.mongo.MongoSettings
import com.geodemo.endpoint.{MasterEndpoint, AdventurerEndpoint, LocationEndpoint}
import com.geodemo.dao.{MongoAdventurerDao, AdventurerDao, MongoLocationDao, LocationDao}
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
  val port = Properties.envOrElse("PORT", "8080").toInt

  val mongoUrl = config.getString("mongodb.url")
  val mongoDbName = config.getString("mongodb.database")

//  val urlList = mongoUrl.split(",").toList.map(new ServerAddress(_))

  val MongoSettings(db) = Some("mongodb://admin:koti3342@ds051007.mongolab.com:51007/heroku_app11453919")

  val locationCollection = db(config.getString("mashqwest.location.collection"))
  val adventurerCollection = db(config.getString("mashqwest.adventurer.collection"))

  val locationDaoM = new MongoLocationDao(locationCollection)
  val adventurerDaoM = new MongoAdventurerDao(adventurerCollection)
  // ///////////// INDEXES for collections go here (include all lookup fields)
  //  configsCollection.ensureIndex(MongoDBObject("customerId" -> 1), "idx_customerId")

  val masterHandler = system.actorOf(
    Props(new MasterEndpoint {
      val locationDao = locationDaoM
      val adventurerDao = adventurerDaoM
    }),
    name = "mashqwest-service"
  )

  // every spray-can HttpServer (and HttpClient) needs an IoWorker for low-level network IO
  // (but several servers and/or clients can share one)
  //override val ioBridge = IOExtension(system).ioBridge

  logger.info("Binding to: " + host + " : " + port)

  newHttpServer(masterHandler) ! Bind(interface = host, port = port)

}

