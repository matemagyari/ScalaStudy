import scala.util.Random

/**
 * Created by mate.magyari on 08/08/2014.
 */
object Garbage extends App {

  sealed abstract class Suite
  case object Spade extends Suite
  case object Heart extends Suite
  case object Club extends Suite
  case object Diamond extends Suite

  sealed abstract class Rank
  case object Two extends Rank
  case object Three extends Rank
  case object Four extends Rank
  case object Five extends Rank
  case object Six extends Rank
  case object Seven extends Rank
  case object Eight extends Rank
  case object Nine extends Rank
  case object Ten extends Rank
  case object Jack extends Rank
  case object Queen extends Rank
  case object King extends Rank
  case object Ace extends Rank

  val suites = Set(Spade, Heart, Club, Diamond)
  val ranks = List(Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace)

  case class Card(rank: Rank, suite: Suite)

  class Deck(pCards: List[Card] = for (r <- ranks; s <- suites) yield Card(r, s)) {

    val cards = if (isValidDeck(pCards)) pCards
    else throw new RuntimeException("Deck is invalid!")

    def shuffle() = new Deck(Random.shuffle(cards))

    def pullFromTop() = (cards.head, new Deck(cards.tail))

    def addToTop(card: Card) = new Deck(card :: cards)

    def addToTop(cardsToAdd: List[Card]) = new Deck(cardsToAdd ::: cards)

    private def isValidDeck(cards: List[Card]) = cards.size <= 52 && cards.distinct.size == cards.size


    case class Person(name:String,age:Int)

    val p1 = Person("Joe",17)
    val p2 = Person("Bill",22)

    //List(p1,p2).filter(_.age > 18).map(_.name)



  }

}

/*

    def remove(rank: Rank) = {
      val (removedCards, rest) = cards.partition(c => c.rank.equals(rank))
      (removedCards, new Deck(rest))
    }

    def remove(suite: Suite) = {
      val (removedCards, rest) = cards.partition(c => c.suite.equals(suite))
      (removedCards, new Deck(rest))
    }

    //def addToBottom(card:Card) = new Deck(cards:+(card))
 */
