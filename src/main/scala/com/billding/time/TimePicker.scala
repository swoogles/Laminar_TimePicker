package com.billding.time

import com.raquo.laminar.api.L._

/**
  * Options:
  *  Styling:
  *    - In-line that shit
  *    - Load a CSS snippet when the component is mounted
  *
  *  UX
  *    - Roll over AM/PM when adjusting hours
  *    - Keep scrolling on long press
  *
  *
  *  Anything else that springs to mind
  */
object TimePicker {

  sealed trait DAY_TIME
  case object AM extends DAY_TIME
  case object PM extends DAY_TIME

  case class Time()

  def Toggler(
    initialValue: DAY_TIME,
  ): (Div, Signal[DAY_TIME]) = {
    val updates = new EventBus[Unit]
    val $value: Var[DAY_TIME] = Var(initialValue)
    val newNumberValues: EventStream[DAY_TIME] =
      updates.events.withCurrentValueOf($value).map {
        curNumberValue =>
          if (curNumberValue == AM)
            PM
          else
            AM
      }

    (
      div(
        cls("amOrPm wheel"),
        button(
          cls(
            "arrival-time adjuster-button open-arrival-time-modal tp-inc",
          ),
          onClick.preventDefault.map(_ => ()) --> updates,
          img(
            cls := "glyphicon",
            src := "/glyphicons/svg/individual-svg/glyphicons-basic-222-chevron-up.svg",
          ),
        ),
        div(
          cls := "tp-display",
          child <-- $value.signal.map(_.toString),
        ),
        button(
          cls := "arrival-time adjuster-button open-arrival-time-modal tp-dec",
          onClick.preventDefault.map(_ => ()) --> updates,
          img(
            cls := "glyphicon",
            src := "/glyphicons/svg/individual-svg/glyphicons-basic-221-chevron-down.svg",
          ),
        ),
        newNumberValues --> $value,
      ),
      $value.signal,
    )

  }

  def NumberPicker(
    initialValue: Int,
    deltaValue: Int,
    minValue: Int,
    maxValue: Int,
    sectionName: String,
  ) = {
    val $numberX: Var[Int] = Var(initialValue)

    val updates = new EventBus[Int]
    val newNumberValues: EventStream[Int] =
      updates.events.withCurrentValueOf($numberX).map {
        case (delta, curNumberValue) =>
          val newValue =
            if (delta > 0)
              if (curNumberValue + delta <= maxValue)
                curNumberValue + delta
              else
                minValue
            else if (curNumberValue + delta >= minValue)
              curNumberValue + delta
            else
              maxValue

          println("New value: " + newValue)
          newValue
      }

    (
      div(
        cls := s"$sectionName wheel",
        button(
          cls := s"arrival-time adjuster-button open-arrival-time-modal tp-inc",
          onClick.preventDefault.map(_ => deltaValue) --> updates,
          img(
            cls := "glyphicon",
            src := "/glyphicons/svg/individual-svg/glyphicons-basic-222-chevron-up.svg",
          ),
        ),
        div(
          cls := s"tp-display",
          child <-- $numberX.signal.map(_.toString),
        ),
        button(
          cls := s"arrival-time adjuster-button open-arrival-time-modal tp-dec",
          onClick.preventDefault.map(_ => -deltaValue) --> updates,
          img(
            cls := "glyphicon",
            src := "/glyphicons/svg/individual-svg/glyphicons-basic-221-chevron-down.svg",
          ),
        ),
        newNumberValues --> $numberX,
      ),
      $numberX.signal,
    )

  }

  def TimePicker(
    time: BusTime,
  ) = {

    val (hourPicker, hourS) =
      NumberPicker(initialValue = time.hours12,
                   deltaValue = 1,
                   minValue = 1,
                   maxValue = 12,
                   sectionName = "hour")
    val (minutePicker, minuteS) =
      NumberPicker(initialValue = time.minutes,
                   deltaValue = 10,
                   minValue = 0,
                   maxValue = 59,
                   sectionName = "minute")
    val (amPmToggler, amOrPm) = Toggler(AM)

    val fullTime: Signal[BusTime] =
      Signal
        .combine(hourS, minuteS, amOrPm)
        .foldLeft(_ => time) {
          case (_, (hours, minutes, amOrPmInner)) => // TODO Use amOrPm
            try {
              // TODO Cleanup
              val offset =
                if (amOrPmInner == AM)
                  0
                else
                  12

              val finalHours =
                if (hours == 12 && amOrPmInner == AM)
                  0
                else if (hours == 12 && amOrPmInner == PM)
                  12
                else
                  hours + offset

              BusTime(s"$finalHours:$minutes")
            } catch {
              case e: Exception =>
                println("busTime parse exception: " + e.getMessage)

                BusTime("10:00")
            }
        }

    (fullTime,
     div(
       cls := "time-picker-simple",
       hourPicker,
       minutePicker,
       amPmToggler,
     ))
  }

}
