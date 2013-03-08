package com.meshqwest.dao

import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.meshqwest.model.Location
import com.meshqwest.mongo.RandomId
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

      val resLat = loc(1).asInstanceOf[Double]
      val resLong = loc(0).asInstanceOf[Double]
      val dist = distFrom(lat, long, resLat, resLong)

      Location(f.getAs[String]("_id"), f.getAs[String]("name").getOrElse(""), resLat, resLong, Some(dist), Some(dist < 100))
      //Location(Some("AAA"), "test", 1, 1)
    })

    logger.info("DAO RESULT: " + res)
    res.toList
  }

  def distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double = {
    val earthRadius = 3958.75
    val dLat = scala.math.toRadians(lat2-lat1)
    val dLng = scala.math.toRadians(lng2-lng1)
    val a = scala.math.sin(dLat/2) * scala.math.sin(dLat/2) +
      scala.math.cos(scala.math.toRadians(lat1)) * scala.math.cos(scala.math.toRadians(lat2)) *
        scala.math.sin(dLng/2) * scala.math.sin(dLng/2)
    val c = 2 * scala.math.atan2(scala.math.sqrt(a), scala.math.sqrt(1-a))
    val dist = earthRadius * c

    val meterConversion = 1609f

    return (dist * meterConversion)
  }

}
