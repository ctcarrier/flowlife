package com.flowlife.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.flowlife.model.TrickCategory
import com.flowlife.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait TrickCategoryDao {

  def save(l: TrickCategory): TrickCategory
  def get(key: String): Option[TrickCategory]
  def getAll: Option[List[TrickCategory]]

}

class MongoTrickCategoryDao(defaultCollection: MongoCollection) extends TrickCategoryDao with Logging {

  def save(l: TrickCategory): TrickCategory = {
    val randomId = RandomId.getNextValue
    val toReturn = l.copy(_id = randomId)
    val toSave = grater[TrickCategory].asDBObject(toReturn)
    defaultCollection.insert(toSave)

    toReturn
  }

  def get(key: String): Option[TrickCategory] = {
    logger.info("In TrickCategory DAO")

    val builder = MongoDBObject.newBuilder
    builder += ("_id" -> key)

    val dbo = defaultCollection.findOne(builder.result.asDBObject)
    val result = dbo.map(f => grater[TrickCategory].asObject(f))

    logger.debug("GET results at DAO: " + result.toString)
    result
  }

  def getAll = {

    val listRes = defaultCollection.find().map(f => {
      grater[TrickCategory].asObject(f)
    }).toList

    val res = listRes match {
      case l: List[TrickCategory] if (!l.isEmpty) => Some(l)
      case _ => None
    }

    res
  }

}
