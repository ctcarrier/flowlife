package com.flowlife.endpoint

import spray.routing.Directives

/**
 * Created with IntelliJ IDEA.
 * User: ccarrier
 * Date: 3/11/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
trait MeshDirectives extends Directives {

  val directGet = path("^[a-zA-Z0-9]+$".r) & get

  val indirectGet = path("") & get

}
