package com.flowlife.boot

import util.Properties
import akka.actor.{ActorSystem, Props}
import com.weiglewilczek.slf4s.Logging
import com.typesafe.config.ConfigFactory
import com.flowlife.mongo.MongoSettings
import com.flowlife.endpoint.{MasterEndpoint, TrickEndpoint, LocationEndpoint}
import com.flowlife.dao._
import spray.can.server.{HttpServer, SprayCanHttpServerApp}
import spray.io.IOExtension
import scala.Some

/**
 * @author chris_carrier
 */

object FlowlifeInitializer extends App with SprayCanHttpServerApp with Logging {

  logger.info("Running Initializer")

  //override lazy val system = ActorSystem("mashqwest")

  val config = ConfigFactory.load()

  val host = "0.0.0.0"
  val port = Properties.envOrElse("PORT", "8080").toInt

  val mongoUrl = config.getString("mongodb.url")
  val mongoDbName = config.getString("mongodb.database")

//  val urlList = mongoUrl.split(",").toList.map(new ServerAddress(_))

  val MongoSettings(db) = Some(config.getString("flowlife.db.url"))

  val trickCollection = db(config.getString("flowlife.trick.collection"))

  val trickDaoM = new MongoTrickDao(trickCollection)
  // ///////////// INDEXES for collections go here (include all lookup fields)
  //  configsCollection.ensureIndex(MongoDBObject("customerId" -> 1), "idx_customerId")

  val masterHandler = system.actorOf(
    Props(new MasterEndpoint {
      val trickDao = trickDaoM
    }),
    name = "flowlife-service"
  )

  // every spray-can HttpServer (and HttpClient) needs an IoWorker for low-level network IO
  // (but several servers and/or clients can share one)
  //override val ioBridge = IOExtension(system).ioBridge

  logger.info("Binding to: " + host + " : " + port)

  newHttpServer(masterHandler) ! Bind(interface = host, port = port)

}

