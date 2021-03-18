package com.billding.time

class BusTimeX {

}

case class Time(private val mins: Int) {
  def addMinutes(delta: Int): Time =
    copy(mins + delta).normalize

  def +(delta: Int): Time = addMinutes(delta)
  def -(delta: Int): Time = addMinutes(-delta)

  private def normalize: Time =
    if (mins < 0 || mins > Time.maxTime.mins) {
      Time(mod(mins, Time.maxTime.mins))
    } else {
      this
    }

  def mod(dividend: Int, divisor: Int): Int =
    ((dividend % divisor) + divisor) % divisor

  def hours: String =
    (mins / 60) % 12 match {
      case 0     => "12"
      case other => other.toString
    }

  def minutes: String = {
    val result = mins % 60
    if (result >= 10)
      result.toString
    else
      s"0${result}"
  }

  def dayTime: DayTime =
    if (mins < Time.highNoon.mins) DayTime.AM
    else DayTime.PM
}

sealed trait Direction

object Direction {
  case object Up   extends Direction
  case object Down extends Direction
}

object Time {
  val highNoon: Time = Time(12 * 60)

  private val maxTime: Time = Time(24 * 60)
}

sealed trait DayTime {
  override def toString: String = this match {
    case DayTime.AM => "AM"
    case DayTime.PM => "PM"
  }
}
object DayTime {
  case object AM extends DayTime
  case object PM extends DayTime
}

object TimeShit {
  import com.raquo.laminar.api.L._
  def wheelView($signal: Signal[String])(updater: Direction => Unit): Div =
    div(
      div(
        "UP",
        opacity(0.5),
        onClick.mapTo(Direction.Up) --> updater
      ),
      child.text <-- $signal,
      div(
        "DOWN",
        opacity(0.5),
        onClick.mapTo(Direction.Down) --> updater
      )
    )

  val timeVar: Var[Time] = Var(Time.highNoon)

  val body: Div =
    div(
      child.text <-- timeVar.signal.map { time =>
        s"${time.hours}:${time.minutes} ${time.dayTime}"
      },
      hr(),
      div(
        wheelView(timeVar.signal.map(_.hours)) {
          case Direction.Up   => timeVar.update(_ + 60)
          case Direction.Down => timeVar.update(_ - 60)
        },
        wheelView(timeVar.signal.map(_.minutes)) {
          case Direction.Up   => timeVar.update(_ + 10)
          case Direction.Down => timeVar.update(_ - 10)
        },
        wheelView(timeVar.signal.map(_.dayTime.toString)) {
          case Direction.Up   => timeVar.update(_ + (60 * 12))
          case Direction.Down => timeVar.update(_ - (60 * 12))
        }
      )
    )
}