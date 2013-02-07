package com.geodemo.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.geodemo.model.Adventurer
import com.geodemo.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait AdventurerDao {

  def save(l: Adventurer): Adventurer

}

class MongoAdventurerDao(defaultCollection: MongoCollection) extends AdventurerDao with Logging {

  def save(l: Adventurer): Adventurer = {
    val randomId = RandomId.getNextValue
    val toSave = MongoDBObject("_id" -> randomId, "name" -> l.name)
    defaultCollection.insert(toSave)

    val toReturn = l.copy(_id = randomId)
    toReturn
  }

  /*def get(distance: Int, lat: Double, long: Double): List[Adventurer] = {
    logger.info("In DAO")

    val query = MongoDBObject("location" -> MongoDBObject("$near" -> (long, lat), "$maxDistance" -> 1))
    val res = defaultCollection.find(query).map(f => {
      val loc: List[Any] = f.as[BasicDBList]("location").toList

      val resLat = loc(1).asInstanceOf[Double]
      val resLong = loc(0).asInstanceOf[Double]
      val dist = distFrom(lat, long, resLat, resLong)

      Adventurer(f.getAs[String]("_id"), f.getAs[String]("name").getOrElse(""), resLat, resLong, Some(dist), Some(dist < 100))
      //Adventurer(Some("AAA"), "test", 1, 1)
    })

    logger.info("DAO RESULT: " + res)
    res.toList
  }*/

}
