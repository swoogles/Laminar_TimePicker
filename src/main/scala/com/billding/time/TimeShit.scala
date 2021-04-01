package com.billding.time

import com.raquo.laminar.api.L
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

  def basicIncVerbiage() =
    span("Inc", styleAttr("font-size: 3vmin"))

  def basicDownVerbiage() =
    span("Dec", styleAttr("font-size: 3vmin"))

  TimePicker.from24hourString(
    initialTime = "09:30",
    incrementRep = span("Inc"),
    decrementRep = span("Dec")
  )

  val body: Div =
    div(
      TimePicker.basicWithTypedTime("08:03") match {
        case TimePickerTyped(component: Div, time: L.Signal[WallTime]) =>
          div(
            child.text <-- time.map("Exposed time: " + _ ) ,
            component
          )
      },
      TimePicker.from24hourString("14:00",
        fancyUpArrowThatShouldBeProvidedByEndUser(),
        fancyDownArrowThatShouldBeProvidedByEndUser()).component,
      TimePicker.from24hourString("21:45",
        basicIncVerbiage(),
        basicDownVerbiage()).component
    )
}
