package com.billding.time

import org.scalajs.dom

import com.raquo.laminar.api.L._

case class TimePicker(
                     component: Div,
                     $time: Signal[String]
                     )

case class TimePickerTyped(
                       component: Div,
                       $time: Signal[WallTime]
                     )

object TimePicker {

  private val style =
"""
<style>
.time-picker {
    font-size: 10vmin;

    /*background-color: lightblue;*/
    width: 100%;
    min-width: 150px;
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
   width:15vmin;
   height:15vmin;
}
.tp-display { grid-area: tp-display; }
.tp-dec {
  grid-area: tp-dec;
  width:15vmin;
  height:15vmin;
}


.plus {
  position: relative;
  width:100%;
  height:100%;
}

.plus:before,
.plus:after {
  content: "";
  width: 100%;
  position:absolute;
  background:#000;
}

/* the vertical line */
.plus:before {
  left:50%;
  top:4px; /* this defines how much black "border" there should be */
  bottom:4px;
  width:  20%;
  min-width: .4rem;
  transform:translateX(-50%);
}

/* the horizontal line */
.plus:after {
  top:50%;
  left:1%;
  right:9%;
  height:  20%;
  min-height: .4rem;
  transform:translateY(-50%);
}

.minus {
  position: relative;
  width:100%;
  height:100%;
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
  height:  20%;
  transform:translateY(-50%);
}
</style>

"""

  dom.document.querySelector("head").innerHTML += style

  private def wheel(
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

  def from24hourString(
             initialTime: String,
             incrementRep: => HtmlElement,
             decrementRep: => HtmlElement,
           ): TimePicker =
    withTypedTime(initialTime, incrementRep, decrementRep) match {
      case TimePickerTyped(component, time) => TimePicker(component,time.map(_.toDumbAmericanString))
    }

  def basicWithTypedTime(
                          initialTime: String,
                        ): TimePickerTyped = {
    withTypedTime(initialTime, basicUpArrow(), basicDownArrow())
  }

  def withTypedTime(
             initialTime: String,
             incrementRep: => HtmlElement,
             decrementRep: => HtmlElement,
           ): TimePickerTyped = {
    val timeVar: Var[WallTime] = Var(WallTime(initialTime))
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

