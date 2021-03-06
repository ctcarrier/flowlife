package com.flowlife.model

import com.mongodb.casbah.commons.MongoDBObject

/**
 * @author chris_carrier
 * @version 9/7/12
 */


case class Location(_id: Option[String], name: String, lat: Double, long: Double, meshId: String, distance: Option[Double] = None, withinTrigger: Option[Boolean] = None)
