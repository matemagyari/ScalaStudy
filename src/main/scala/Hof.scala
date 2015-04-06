/**
 * Created by mate.magyari on 29/09/2014.
 */
object Hof extends App {

  def logger(f: (Int, Int) => Int) =
    (a: Int, b: Int) => {
      println("Hello")
      f.apply(a, b)
    }

  def add(a: Int, b: Int) = a + b

  println(logger(add)(3, 4))
}
