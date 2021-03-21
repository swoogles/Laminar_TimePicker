package com.billding.time

/*
case class Time(private val mins: Int) {
  def addMinutes(delta: Int): Time =
    copy(mins + delta).normalize

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

  def dayTime: DayTime =
    if (mins < Time.highNoon.mins) DayTime.AM
    else DayTime.PM
}

 */

sealed trait Direction

object Direction {
  case object Up   extends Direction
  case object Down extends Direction
}

/*
object Time {
  val highNoon: Time = Time(12 * 60)

  private val maxTime: Time = Time(24 * 60)
}

 */

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

  def basicUpArrow() =
    span("+")

  val fancyUpArrowThatShouldBeProvidedByEndUser =
    img(
      cls := "glyphicon",
      src := "/src/main/resources/icons/glyphicons-basic-222-chevron-up.svg",
    )

  def wheel($signal: Signal[String], updater: Int => Unit, delta: Int, upButtonRep: HtmlElement): Div =
    div(
      cls("wheel"),
      button(
        cls(
          "tp-inc",
        ),
        onClick.mapTo(delta) --> updater,
        upButtonRep
      ),
      div(
        cls := "tp-display",
        child <-- $signal.map(_.toString),
      ),
      button(
        cls := "tp-dec",
        onClick.mapTo(-delta) --> updater,
        img(
          cls := "glyphicon",
          src := "/src/main/resources/icons/glyphicons-basic-221-chevron-down.svg",
        ),
      )
    )

  val timeVar: Var[BusTime] = Var(BusTime("12:00"))

  val updater =
    (minutes: Int) => timeVar.update(_.plusMinutes(minutes))

  def activeUpArrow() =
    basicUpArrow()

  val body: Div =
    div(
      child.text <-- timeVar.signal.map { time =>
        time.toDumbAmericanString
      },
      hr(),
      div(
        cls := "time-picker-simple",
        wheel($signal = timeVar.signal.map(_.hours12.toString),
          updater = updater,
          delta = 60,
          activeUpArrow())
          .amend(cls("hour")),
        wheel($signal = timeVar.signal.map(_.minutes.toString),
          updater = updater,
          delta = 5,
          activeUpArrow())
          .amend(cls("minute")),
        wheel($signal = timeVar.signal.map(_.dayTime.toString),
          updater = updater,
          delta = 60*12,
          activeUpArrow())
          .amend(cls("amOrPm")),
      )
    )
}