package com.billding.time

import org.scalajs.dom

import com.raquo.laminar.api.L._

case class TimePicker(
                     component: Div,
                     $time: Signal[String]
                     )

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

  def wheel(
             $signal: Signal[Any],
             updater: Int => Unit,
             delta: Int,
             upButtonRep: HtmlElement,
             downButtonRep: HtmlElement,
           ): Div =
    div(
      cls("wheel"),
      button(
        cls(
          "tp-inc",
        ),
        onClick.mapTo(delta) --> updater,
        upButtonRep,
      ),
      div(
        cls := "tp-display",
        child <-- $signal.map(_.toString),
      ),
      button(
        cls := "tp-dec",
        onClick.mapTo(-delta) --> updater,
        downButtonRep,
      ),
    )

  private def basicUpArrow() =
    div("+")

  private def basicDownArrow() =
    div("-")

  def withPlusMinusButtons(
           initialTime: String,
           ) =
    apply(initialTime, basicUpArrow(), basicDownArrow())

  def apply(
             initialTime: String,
             incrementRep: => HtmlElement,
             decrementRep: => HtmlElement,
           ): TimePicker = {
    val timeVar: Var[BusTime] = Var(BusTime(initialTime))
    val updater =
      (minutes: Int) => timeVar.update(_.plusMinutes(minutes))
TimePicker(
    div(
      cls := "time-picker-simple",
      wheel($signal = timeVar.signal.map(_.hours12),
        updater = updater,
        delta = 60,
        incrementRep,
        decrementRep)
        .amend(cls("hour")),
      wheel($signal = timeVar.signal.map(_.paddedMinutes),
        updater = updater,
        delta = 5,
        incrementRep,
        decrementRep)
        .amend(cls("minute")),
      wheel($signal = timeVar.signal.map(_.dayTime),
        updater = updater,
        delta = 60 * 12,
        incrementRep,
        decrementRep)
        .amend(cls("amOrPm")),
    ),
  timeVar.signal.map(_.toDumbAmericanString)
)
  }
}

