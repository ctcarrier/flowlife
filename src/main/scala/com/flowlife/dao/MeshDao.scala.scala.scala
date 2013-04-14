package com.flowlife.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.flowlife.model.Mesh
import com.flowlife.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait MeshDao {

  def save(l: Mesh): Mesh
  def get(key: String): Option[Mesh]

}

class MongoMeshDao(defaultCollection: MongoCollection) extends MeshDao with Logging {

  def save(l: Mesh): Mesh = {
    val randomId = RandomId.getNextValue
    val toReturn = l.copy(_id = randomId)
    val toSave = grater[Mesh].asDBObject(toReturn)
    defaultCollection.insert(toSave)

    toReturn
  }

  def get(key: String): Option[Mesh] = {
    logger.info("In Mesh DAO")

    val builder = MongoDBObject.newBuilder
    builder += ("_id" -> key)

    val dbo = defaultCollection.findOne(builder.result.asDBObject)
    val result = dbo.map(f => grater[Mesh].asObject(f))

    logger.debug("GET results at DAO: " + result.toString)
    result
  }

}
