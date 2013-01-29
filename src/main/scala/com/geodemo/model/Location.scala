package com.geodemo.model

import com.mongodb.casbah.commons.MongoDBObject

/**
 * @author chris_carrier
 * @version 9/7/12
 */


case class Location(_id: Option[String], name: String, lat: Int, long: Int)
