package com.billding.time

import org.scalajs.dom

import com.raquo.laminar.api.L._

case class TimePicker(
                     component: Div,
                     $time: Signal[String]
                     )

case class TimePickerTyped(
                       component: Div,
                       $time: Signal[BusTime]
                     )

object TimePicker {

  val style =
"""
<style>
.time-picker {
    font-size: 3.5rem;

    /*background-color: lightblue;*/
    width: 100%;
    min-width: 150px;
    max-width: 450px;
    display: grid;
    grid-template-columns: minmax(2.5rem, 30%) minmax(2.5rem, 30%) minmax(2.5rem, 30%);
    grid-template-rows: auto 1fr ;
    grid-template-areas:
            "hour       minute      amOrPm";
    padding: 5px;
}

.hour { grid-area: hour; }
.minute { grid-area: minute; }
.amOrPm { grid-area: amOrPm; }

.wheel {
    /*background-color: pink;*/
    display: grid;
    place-items: center;
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr ;
    /*grid-template-columns: minmax(80px, 150px);*/
    grid-template-areas:
            "tp-inc"
            "tp-display"
            "tp-dec";
    padding: 5px;
}

.tp-inc {
   grid-area: tp-inc;
}
.tp-display { grid-area: tp-display; }
.tp-dec { grid-area: tp-dec; }


.plus {
  position: relative;
  width:3.5em;
  height:3.5em;
}

.plus:before,
.plus:after {
  content: "";
  position:absolute;
  background:#000;
}

/* the vertical line */
.plus:before {
  left:50%;
  top:4px; /* this defines how much black "border" there should be */
  bottom:4px;
  width:.4em;
  transform:translateX(-50%);
}

/* the horizontal line */
.plus:after {
  top:50%;
  left:4px;
  right:4px;
  height:.4em;
  transform:translateY(-50%);
}

.minus {
  position: relative;
  width:3.5em;
  height:3.5em;
}

.minus:before,
.minus:after {
  content: "";
  position:absolute;
  background:#000;
}

/* the horizontal line */
.minus:after {
  top:50%;
  left:4px;
  right:4px;
  height:.4em;
  transform:translateY(-50%);
}
</style>

"""

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
    div(cls("plus"))

  private def basicDownArrow() =
    div(cls("minus"))

  def withPlusMinusButtons(
           initialTime: String,
           ) =
    apply(initialTime, basicUpArrow(), basicDownArrow())

  def apply(
             initialTime: String,
             incrementRep: => HtmlElement = basicUpArrow(),
             decrementRep: => HtmlElement = basicDownArrow(),
           ): TimePicker =
    basicWithTypedTime(initialTime, incrementRep, decrementRep) match {
      case TimePickerTyped(component, time) => TimePicker(component,time.map(_.toDumbAmericanString))
    }

  def basicWithTypedTime(
             initialTime: String,
             incrementRep: => HtmlElement = basicUpArrow(),
             decrementRep: => HtmlElement = basicDownArrow(),
           ): TimePickerTyped = {
    val timeVar: Var[BusTime] = Var(BusTime(initialTime))
    val updater =
      (minutes: Int) => timeVar.update(_.plusMinutes(minutes))
    TimePickerTyped(
      div(
        cls := "time-picker",
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
      timeVar.signal
    )
  }
}

