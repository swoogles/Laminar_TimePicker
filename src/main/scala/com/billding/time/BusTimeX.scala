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

object TimeShit {

  import com.raquo.laminar.api.L._

  def basicUpArrow() =
    span("+")

  def basicDownArrow() =
    span("-")

  def fancyUpArrowThatShouldBeProvidedByEndUser() =
    img(
      cls := "glyphicon",
      src := "/src/main/resources/icons/glyphicons-basic-222-chevron-up.svg",
    )

  def fancyDownArrowThatShouldBeProvidedByEndUser() =
    img(
      cls := "glyphicon",
      src := "/src/main/resources/icons/glyphicons-basic-221-chevron-down.svg",
    )

  def activeUpArrow() =
    basicUpArrow()

  val body: Div =
    div(
      TimePicker("08:03", basicUpArrow(), basicDownArrow()),
      TimePicker("14:00",
        fancyUpArrowThatShouldBeProvidedByEndUser,
        fancyDownArrowThatShouldBeProvidedByEndUser),
    )
}
