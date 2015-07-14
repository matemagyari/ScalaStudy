package org.home

object Solution {

   def solution(a: Array[Int]): Int = helper(a.toList, 0)


   def helper(a: List[Int], index: Int): Int = {
     println("index: " + index)
     if (index < a.length) {
       a match {
         case Nil => -1
         case head :: Nil =>
           if (head == a.slice(0, a.length - 1).length) index
           else -1
         case head :: tail =>
           val before = a.slice(0, index)
           val after = a.slice(index+1, a.length)
           println("before" + before + " " + before.sum)
           println("after" + after + " " + after.sum)
           println("\n\n")
           if (after.sum == before.sum) index else helper(a, index + 1)
       }
     } else -1
   }


 }

object Runner extends App {
  println("hi" + Solution.solution(Array(-1, 3, -4, 5, 1, -6, 2, 1)))
}
