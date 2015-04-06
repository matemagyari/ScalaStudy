/**
 * Created by mate.magyari on 09/11/2014.
 */
object FunctionGrouping extends App {


  //----------- Version I - group functions in a class and pass the object instance to the client ----------------
  // pro 1: grouping on the client side too
  // pro 2: polimorphism
  trait MathBox {
    def sum(numbers:List[Int])
    def multiple(numbers:List[Int])
  }
  class SimpleMathBox extends MathBox {
    def sum(numbers:List[Int]) = numbers.foldLeft(0)(_+_)
    def multiple(numbers:List[Int]) = numbers.foldLeft(0)(_*_)
  }
  class ParallelizingMathBox extends MathBox {
    def sum(numbers:List[Int]) = 2
    def multiple(numbers:List[Int]) = 3
  }
  
  def client3(numbers:List[Int], mathBox:MathBox) {
    println("Sum is " + mathBox.sum(numbers))
    println("Multi is " + mathBox.multiple(numbers))
  }
  
  //----------- Version II - no objects, just functions passed to clients ----------------------
  //pro 1: polimorphism made simple. We can pass any function with the given signature
  //pro 2: there is some grouping in function definition

  object SimpleMathBoxObject {
    def sum(numbers:List[Int]) = numbers.foldLeft(0)(_+_)
    def multiple(numbers:List[Int]) = numbers.foldLeft(0)(_*_)
  }
  object ParallelizingMathBoxObject {
    def sum(numbers:List[Int]) = 6
    def multiple(numbers:List[Int]) = 7
  }

  def client1(numbers:List[Int], sum:(List[Int]=>Int), multiple:(List[Int]=>Int)) {
    println("Sum is " + sum(numbers))
    println("Multi is " + multiple(numbers))
  }

  client1(List(1,2,3),SimpleMathBoxObject.sum,SimpleMathBoxObject.multiple)

  //----------- Version II - group functions in an object and pass the object to the client ----------------
  //pro: grouping in both places
  //con: no polimorphism

  //great, but no polimorphism
  def client2(numbers:List[Int]) {
    println("Sum is " + SimpleMathBoxObject.sum(numbers))
    println("Multi is " + SimpleMathBoxObject.multiple(numbers))
  }

  //---------- functions with common dependencies

}
