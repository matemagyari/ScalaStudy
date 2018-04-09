package org.home.fpvsoop

import org.home.fpvsoop.OopVsFp.{Board, Direction, Down, Left, Right, Up}

object OopVsFp {

  sealed trait Direction
  case object Up extends Direction
  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction

  trait Board {
    def plantMine(): Unit
  }

  class Player(id: Int, initX: Int, initY: Int, board: Board) {

    private var x: Int = initX
    private var y: Int = initY

    def move(d: Direction): Unit = d match {
      case Up ⇒ y = y + 1
      case Down ⇒ y = y - 1
      case Left ⇒ x = x - 1
      case Right ⇒ x = x + 1
    }

    def dropStuff(): Unit = {
      board.plantMine()
    }
  }

}

object OopVsFp2 {

  case class Player(id: Int, x: Int, y: Int, board: Board) {

    def move(d: Direction): Player =  d match {
      case Up ⇒ copy(y = y + 1)
      case Down ⇒ copy(y = y - 1)
      case Left ⇒ copy(x = x - 1)
      case Right ⇒ copy(x = x + 1)
    }
  }
}
