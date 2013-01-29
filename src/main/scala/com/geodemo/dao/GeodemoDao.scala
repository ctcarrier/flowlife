package com.geodemo.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import scala.reflect.Manifest
import com.weiglewilczek.slf4s.Logging
import akka.actor.ActorRef
import com.geodemo.mongo.RandomId
import akka.dispatch.ExecutionContext

trait GeodemoDao[T <: CaseClass, W <: CaseClass] extends Logging {
//  val mongoCollection: MongoCollection
//
  val localExecutionContext: ExecutionContext
//
//  def urlPrefix: String
//  def formatKeyAsId(s: String): String = {
//    urlPrefix + s
//  }
//  def nextRandomId: String = {
//    formatKeyAsId(RandomId.getNextValue.get)
//  }
//
//  def getByKey(key: String): Future[Option[T]] = {
//    getByKey(key, None)
//  }
//  def getByKey(key: String, userId: Option[String]): Future[Option[T]] = {
//    get(formatKeyAsId(key), userId)
//  }
//  def getSometing(id: String, userId: Option[String])(implicit man: Manifest[W]): Future[Option[W]] = {
//    get[W](id, userId)
//  }
//  def get[TT <: W](id: String, userId: Option[String] = None)(implicit man: Manifest[TT]): Future[Option[TT]] = {
//    Future {
//      val builder = MongoDBObject.newBuilder
//      builder += ("_id" -> id)
//      userId.foreach(builder += "content.userUrl" -> _)
//
//      val dbo = mongoCollection.findOne(builder.result.asDBObject)
//      val result = dbo.map(f => grater[TT].asObject(f))
//
//      logger.debug("GET results at DAO: " + result.toString)
//      result
//    }
//  }
//
//  def create[TT <: W](wrapper: TT)(implicit man: Manifest[TT]): Future[Option[TT]] = {
//    Future {
//      val dbo = grater[TT].asDBObject(wrapper)
//      val builder = MongoDBObject.newBuilder
//      builder ++= dbo.toList
//      builder += ("_id" -> Some(nextRandomId))
//      val toSave = builder.result
//      mongoCollection += toSave
//      val res = grater[TT].asObject(toSave)
//      Some(res)
//    }
//  }
//
//  def update[TT <: T, WW <: W](key: String, model: TT)(implicit man: Manifest[TT], manW: Manifest[WW]): Future[Option[WW]] = {
//    Future {
//      val inputDbo = grater[TT].asDBObject(model)
//      val query = MongoDBObject("_id" -> formatKeyAsId(key))
//      val update = $set("content" -> List(inputDbo))
//
//      mongoCollection.update(query, update, false, false, WriteConcern.Safe)
//
//      val dbo = mongoCollection.findOne(query)
//      val result = dbo.map(f => grater[WW].asObject(f))
//
//      result
//    }
//  }
}