/**
 * Created by mate.magyari on 06/08/2014.
 */
object Scopes extends App {


}
package inf {

import inf.app.A

package app {

    class A {
      private[app] val x = 5
    }

    class B {
      val b = new A().x
    }
  }

  class C {
    //can't see it here
    //val c = new A().x
  }
}
