package com.flowlife.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.flowlife.model.Trick
import com.flowlife.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList
import spray.routing.authentication.UserPass

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait UserDao {

  def save(l: UserPass): UserPass
  def get(key: String): Option[UserPass]
  def getAll(category: Option[String]): Option[List[UserPass]]

}

class MongoUserDao(defaultCollection: MongoCollection) extends UserDao with Logging {

  def save(l: UserPass): UserPass = {
    val randomId = RandomId.getNextValue
    val toReturn = l.copy()
    val toSave = grater[UserPass].asDBObject(toReturn)
    defaultCollection.insert(toSave)

    toReturn
  }

  def get(key: String): Option[UserPass] = {
    logger.info("In UserPass DAO")

    val builder = MongoDBObject.newBuilder
    builder += ("_id" -> key)

    getFromObj(builder.result.asDBObject)
  }

  private def getFromObj(obj: MongoDBObject): Option[UserPass] = {
    val dbo = defaultCollection.findOne(obj)
    val result = dbo.map(f => grater[UserPass].asObject(f))

    logger.debug("GET results at DAO: " + result.toString)
    result
  }

  def getAll(category: Option[String]) = {
    val builder = MongoDBObject.newBuilder
    category.foreach(c => builder += ("category" -> c))

    val listRes = defaultCollection.find(builder.result.asDBObject).map(f => {
      grater[UserPass].asObject(f)
    }).toList

    val res = listRes match {
      case l: List[UserPass] if (!l.isEmpty) => Some(l)
      case _ => None
    }

    res
  }

}
