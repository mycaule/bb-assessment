package mowitnow

import org.scalatest._
import Mower._
import Mower.Direction._
import Mower.Command._

class MowerSpec extends FlatSpec with Matchers {
  val lawn = Lawn(5, 5)
  val pos1 = Position(1, 2, North)
  val pos2 = Position(5, 1, East)

  it should "parse and serialize positions" in {
    pos1.toString shouldBe "1 2 N"
    pos2.toString shouldBe "5 1 E"

    Position.parse("1 2 N") shouldBe Some(pos1)
    Position.parse("5 1 E") shouldBe Some(pos2)
    Position.parse("? ? ?") shouldBe None
  }

  it should "parse instructions" in {
    Command.parse("GAGA") shouldBe List(Left, Forward, Left, Forward)
    Command.parse("ADAD") shouldBe List(Forward, Right, Forward, Right)
    Command.parse("????") shouldBe List()
  }

  it should "parse lawns" in {
    Lawn.parse("5 5") shouldBe Some(Lawn(5, 5))
    Lawn.parse("? ?") shouldBe None
  }

  it should "translate objects" in {
    Mower.move(lawn)(pos1, Forward) shouldBe Position(1, 3, North)
    Mower.move(lawn)(pos2, Forward) shouldBe Position(5, 1, East)
  }

  it should "rotate objects" in {
    Mower.move(lawn)(pos1, Left) shouldBe Position(1, 2, West)
    Mower.move(lawn)(pos2, Right) shouldBe Position(5, 1, South)
  }

  it should "check if objects are inside the lawn" in {
    lawn.check(Position(5, -1, North)) shouldBe false
    lawn.check(Position(10, 5, North)) shouldBe false
    lawn.check(Position(5, 5, North)) shouldBe true
  }

  it should "solve simple examples" in {
    Command.parse("GAGAGAGAA").foldLeft(Position(1, 2, North))(Mower.move(lawn)) shouldBe Position(1, 3, North)
    Command.parse("AADAADADDA").foldLeft(Position(3, 3, East))(Mower.move(lawn)) shouldBe Position(5, 1, East)
  }
}
