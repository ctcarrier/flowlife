package com.geodemo.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.geodemo.model.Location
import com.geodemo.mongo.RandomId
import com.weiglewilczek.slf4s.Logging
import com.mongodb.BasicDBList

/**
 * @author chris_carrier
 * @version 7/25/12
 */

trait LocationDao {

  def saveLocation(l: Location): Location
  def getNearbyLocations(distance: Int, lat: Double, long: Double): List[Location]

}

class MongoLocationDao(defaultCollection: MongoCollection) extends LocationDao with Logging {

  def saveLocation(l: Location): Location = {
    val randomId = RandomId.getNextValue
    val toSave = MongoDBObject("_id" -> randomId, "name" -> l.name, "location" -> List(l.long, l.lat))
    defaultCollection.insert(toSave)

    val toReturn = l.copy(_id = randomId)
    toReturn
  }

  def getNearbyLocations(distance: Int, lat: Double, long: Double): List[Location] = {
    logger.info("In DAO")

    val query = MongoDBObject("location" -> MongoDBObject("$near" -> (long, lat), "$maxDistance" -> 1))
    val res = defaultCollection.find(query).map(f => {
      val loc: List[Any] = f.as[BasicDBList]("location").toList

      Location(f.getAs[String]("_id"), f.getAs[String]("name").getOrElse(""), loc(1).asInstanceOf[Double], loc(0).asInstanceOf[Double])
      //Location(Some("AAA"), "test", 1, 1)
    })

    logger.info("DAO RESULT: " + res)
    res.toList
  }

}
