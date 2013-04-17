package com.flowlife.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.flowlife.model.Trick
import com.flowlife.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait TrickDao {

  def save(l: Trick): Trick
  def get(key: String): Option[Trick]
  def getAll: Option[List[Trick]]

}

class MongoTrickDao(defaultCollection: MongoCollection) extends TrickDao with Logging {

  def save(l: Trick): Trick = {
    val randomId = RandomId.getNextValue
    val toReturn = l.copy(_id = randomId)
    val toSave = grater[Trick].asDBObject(toReturn)
    defaultCollection.insert(toSave)

    toReturn
  }

  def get(key: String): Option[Trick] = {
    logger.info("In Trick DAO")

    val builder = MongoDBObject.newBuilder
    builder += ("_id" -> key)

    val dbo = defaultCollection.findOne(builder.result.asDBObject)
    val result = dbo.map(f => grater[Trick].asObject(f))

    logger.debug("GET results at DAO: " + result.toString)
    result
  }

  def getAll = {

    val listRes = defaultCollection.find().map(f => {
      grater[Trick].asObject(f)
    }).toList

    val res = listRes match {
      case l: List[Trick] if (!l.isEmpty) => Some(l)
      case _ => None
    }

    res
  }

}
