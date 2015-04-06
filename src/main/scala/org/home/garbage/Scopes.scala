/**
 * Created by mate.magyari on 06/08/2014.
 */
object Scopes extends App {


}
package org.home.garbage

class C {
    //can't see it here
    //val c = new A().x
  }

    class A {
      private[app] val x = 5
    }

  class B {
      val b = new A().x
    }
