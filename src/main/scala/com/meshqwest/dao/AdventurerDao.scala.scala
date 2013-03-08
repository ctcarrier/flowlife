package com.meshqwest.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.meshqwest.model.Adventurer
import com.meshqwest.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait AdventurerDao {

  def save(l: Adventurer): Adventurer
  def get(key: String): Option[Adventurer]

}

class MongoAdventurerDao(defaultCollection: MongoCollection) extends AdventurerDao with Logging {

  def save(l: Adventurer): Adventurer = {
    val randomId = RandomId.getNextValue
    val toSave = MongoDBObject("_id" -> randomId, "name" -> l.name)
    defaultCollection.insert(toSave)

    val toReturn = l.copy(_id = randomId)
    toReturn
  }

  def get(key: String): Option[Adventurer] = {
    logger.info("In Adventurer DAO")

    val builder = MongoDBObject.newBuilder
    builder += ("_id" -> key)

    val dbo = defaultCollection.findOne(builder.result.asDBObject)
    val result = dbo.map(f => grater[Adventurer].asObject(f))

    logger.debug("GET results at DAO: " + result.toString)
    result
  }

}
