package com.billding.time

import org.scalajs.dom

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

/*
object Time {
  val highNoon: Time = Time(12 * 60)

  private val maxTime: Time = Time(24 * 60)
}

 */

sealed trait DayTime
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

  def wheel($signal: Signal[Any], updater: Int => Unit, delta: Int, upButtonRep: HtmlElement): Div =
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

  def activeUpArrow() =
    basicUpArrow()

  object TimePicker {
    val style =
      """
        |<style>
        |body {
        |  background-color: linen;
        |}
        |
        |h1 {
        |  color: maroon;
        |  margin-left: 40px;
        |}
        |
        |</style>
        |""".stripMargin
    dom.document.querySelector("head").innerHTML += style

    def apply(initialTime: String) = {
      val timeVar: Var[BusTime] = Var(BusTime(initialTime))
      val updater =
        (minutes: Int) => timeVar.update(_.plusMinutes(minutes))

      div(
        cls := "time-picker-simple",
        wheel($signal = timeVar.signal.map(_.hours12),
          updater = updater,
          delta = 60,
          activeUpArrow())
          .amend(cls("hour")),
        wheel($signal = timeVar.signal.map(_.paddedMinutes),
          updater = updater,
          delta = 5,
          activeUpArrow())
          .amend(cls("minute")),
        wheel($signal = timeVar.signal.map(_.dayTime),
          updater = updater,
          delta = 60 * 12,
          activeUpArrow())
          .amend(cls("amOrPm")),
      )
    }
  }

  val body: Div =
    div(
      TimePicker("08:00"),
      TimePicker("14:00")
    )
}