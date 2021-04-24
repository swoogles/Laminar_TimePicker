package com.billding.time

import com.raquo.laminar.api.L._
import animus._

case class TimePicker[T](
  component: Div,
  $time: Signal[T])

object TimePicker {
  def apply(
             initialTime: String,
             incrementRep: => HtmlElement,
             decrementRep: => HtmlElement,
           ): TimePicker[String] =
    apply(initialTime, incrementRep, decrementRep, x=>x, 5)

  def apply(
             initialTime: String,
           ): TimePicker[WallTime] =
    apply(initialTime, basicUpArrow(), basicDownArrow(), WallTime(_), 5)

  def apply(
             initialTime: String,
             minuteDelta: Int
           ): TimePicker[WallTime] =
    apply(initialTime, basicUpArrow(), basicDownArrow(), WallTime(_), minuteDelta)


  def apply[T](
                initialTime: String,
                incrementRep: => HtmlElement,
                decrementRep: => HtmlElement,
                typer: String => T,
                minuteDelta: Int,
              ): TimePicker[T] = {
    val initialTimeTyped = WallTime(initialTime)
    val timeVar: Var[WallTime] = Var(initialTimeTyped)
    val updater =
      (minutes: Int) => timeVar.update(_.plusMinutes(minutes))
    TimePicker(
      div(
        cls := "time-picker",
        wheelAnimated(
          $signal = timeVar.signal.map(_.hours.toString.split("").toList),
          updater = updater,
          delta = 60,
          incrementRep,
          decrementRep)
          .amend(cls("hour")),
        phauxWheel(),
        wheelAnimated($signal = timeVar.signal.map(_.paddedMinutes.split("").toList),
          updater = updater,
          delta = minuteDelta,
          incrementRep,
          decrementRep)
          .amend(cls("minute")),

        initialTimeTyped.hourNotation match {
          case HourNotation.Twelve =>
            wheelAnimated($signal = timeVar.signal.map(dayTime => List(dayTime.dayTime)),
              updater = updater,
              delta = 60 * 12,
              incrementRep,
              decrementRep)
              .amend(cls("amOrPm"))
          case HourNotation.TwentyFour => div()
        }
      ),
      timeVar.signal.map(time => typer(time.toEUString)),
    )
  }

  private val style =
    """
<style>
.time-picker {
    font-size: 10vmin;

    /*background-color: lightblue;*/
    width: 100%;
    min-width: 150px;
    display: grid;
    grid-template-columns: minmax(2.5rem, 30%) minmax(1.0rem, 10%) minmax(2.5rem, 30%) minmax(2.5rem, 30%);
    grid-template-rows: auto 1fr ;
    grid-template-areas:
            "hour  colon  minute  amOrPm";
    padding: 5px;
}

.hour { grid-area: hour; }
.colon {
   display: grid;
   place-items: center;
   grid-area: colon;
}
.minute { grid-area: minute; }
.amOrPm { grid-area: amOrPm; }

.wheel {
    display: grid;
    place-items: center;
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr ;
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

  org.scalajs.dom.document.querySelector("head").innerHTML += style

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

  private def wheelAnimated(
                     $signal: Signal[List[String]],
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
        AnimatedTicker($signal),
//        child <-- $signal.map(_.toString),
      ),
      button(
        cls := "tp-dec",
        onClick.mapTo(-delta) --> updater,
        downButtonRep,
      ),
    )

  private def phauxWheel(): Div =
    div(
      cls("colon"),
      ":"
    )

  private def basicUpArrow() =
    div(cls("plus"))

  private def basicDownArrow() =
    div(cls("minus"))

}
