package com.flowlife.model

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 5/28/13
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
case class User(email: String, password: String, role: Option[String])
