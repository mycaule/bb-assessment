package mowitnow

object Mower {
  object Command extends Enumeration {
    type Command = Value
    val Left = Value("G")
    val Right = Value("D")
    val Forward = Value("A")

    def parse(s: String) = s.toList.map(_.toString).map(this.withName)
  }

  object Direction extends Enumeration {
    type Direction = Value
    val North = Value("N")
    val South = Value("S")
    val East = Value("E")
    val West = Value("W")
  }

  case class Lawn(x: Integer, y: Integer) {
    def check(pos: Position) = 0 <= pos.x && pos.x <= x && 0 <= pos.y && pos.y <= y
  }

  object Lawn {
    def parse(s: String) = {
      val pattern = "([0-9]+) ([0-9]+)".r

      s match {
        case pattern(x, y) => Some(Lawn(x.toInt, y.toInt))
        case _             => None
      }
    }
  }

  case class Position(x: Integer, y: Integer, d: Direction.Direction) {
    override def toString = s"$x $y $d"
  }

  object Position {
    def parse(s: String) = {
      val pattern = "([0-9]+) ([0-9]+) ([A-Z]+)".r

      s match {
        case pattern(x, y, d) => Some(Position(x.toInt, y.toInt, Direction.withName(d)))
        case _                => None
      }
    }
  }

  def move(pos: Position, cmd: Command.Command) = {
    cmd match {
      case Command.Left => pos.d match {
        case Direction.North => pos.copy(d = Direction.West)
        case Direction.South => pos.copy(d = Direction.East)
        case Direction.East  => pos.copy(d = Direction.North)
        case Direction.West  => pos.copy(d = Direction.South)
      }
      case Command.Right => pos.d match {
        case Direction.North => pos.copy(d = Direction.East)
        case Direction.South => pos.copy(d = Direction.West)
        case Direction.East  => pos.copy(d = Direction.South)
        case Direction.West  => pos.copy(d = Direction.North)
      }
      case Command.Forward => pos.d match {
        case Direction.North => pos.copy(y = pos.y + 1)
        case Direction.South => pos.copy(y = pos.y - 1)
        case Direction.East  => pos.copy(x = pos.x + 1)
        case Direction.West  => pos.copy(x = pos.x - 1)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    // val str1: String = scala.ioStdIn.readLine
    val lawn = Lawn(5, 5) // TODO

    // val str2: String = scala.io.StdIn.readLine
    // val str3: String = scala.io.StdIn.readLine
    val program: Seq[(Position, Seq[Command.Command])] = Seq(
      (Position(1, 2, Direction.North), Command.parse("GAGAGAGAA")),
      (Position(3, 3, Direction.East), Command.parse("AADAADADDA"))
    ) // TODO

    val positions: Seq[Position] = program.map { case (pos, cmds) => cmds.foldLeft(pos)(Mower.move) }

    println("Positions finales:")
    positions.foreach(println)
  }
}
