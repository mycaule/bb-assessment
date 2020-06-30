package mowitnow

object Mower {
  object Command extends Enumeration {
    type Command = Value
    val Left = Value("L")
    val Right = Value("R")
    val Forward = Value("F")

    def parse(s: String) = scala.util.Try(s.toList.map(_.toString).map(this.withName)).getOrElse(Nil)
  }

  object Direction extends Enumeration {
    type Direction = Value
    val North = Value("N")
    val South = Value("S")
    val East = Value("E")
    val West = Value("W")
  }

  case class Lawn(x: Integer, y: Integer) {
    def check(pos: Position): Boolean = (0 <= pos.x && pos.x <= x && 0 <= pos.y && pos.y <= y)
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

  def move(lawn: Lawn)(pos: Position, cmd: Command.Command) = {
    val newPos = cmd match {
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

    // La tondeuse ne bouge pas si on essaie de la faire sortir
    if (lawn.check(newPos)) newPos else pos
  }

  def main(args: Array[String]): Unit = {
    val filename = if (args.size > 0) args(0) else "examples/program.mow"
    val file = scala.io.Source.fromFile(filename).getLines

    // Configuration
    val DEBUG = false

    Lawn.parse(file.next) match {
      case None => println("The shape of the lawn could not be parsed.")
      case Some(lawn) =>
        println(s"Lawn of shape ${lawn.x}x${lawn.y}")

        var pos: Option[Position] = None
        var cmds: Seq[Command.Command] = Nil

        // Basic strategy to parse a file by pairs of lines that can be invalid
        file.foreach(line => {
          pos = Position.parse(line) match {
            case p if p.isDefined => p
            case _                => pos
          }

          cmds = Command.parse(line) match {
            case l if l.nonEmpty => l
            case _               => cmds
          }

          (pos, cmds) match {
            case (Some(oldP), c) if c.nonEmpty =>
              val newP = cmds.foldLeft(oldP)(Mower.move(lawn))
              println(s"$newP")

              if (DEBUG) println(s" :: $oldP ==> $newP via ${cmds.mkString}")

              pos = Some(newP)
              cmds = Nil
            case _ => ()
          }
        })
    }
  }
}
