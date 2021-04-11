package com.billding.time

import scala.util.matching.Regex

case class Minutes private (
  value: Int) {

  def /(
    other: Int,
  ) = value / other

  def isBefore(
    m: Minutes,
  ): Boolean = value < m.value

  def isAfter(
    m: Minutes,
  ): Boolean = value > m.value
}

object Minutes {
  private val max = 24 * 60

  def safe(
    value: Int,
  ): Minutes =
    Minutes(mod(value, max))

  private def mod(
    dividend: Int,
    divisor: Int,
  ): Int =
    ((dividend % divisor) + divisor) % divisor

}

sealed trait HourNotation

object HourNotation {
  case object Twelve extends HourNotation

  case object TwentyFour extends HourNotation
}

case class WallTime private (
  localTime: Minutes /*numMinutes*/,
  hourNotation: HourNotation) {
  private val hours24: Int = localTime / 60

  private val hours12: Int =
    if (hours24 == 0 || hours24 == 12)
      12
    else hours24 % 12

  val hours: Int = hourNotation match {
    case HourNotation.Twelve => hours12
    case HourNotation.TwentyFour => hours24
  }

  val minutes: Int = localTime.value % 60

  val dayTime: String =
    if (hours24 > 11)
      DayTime.PM.toString
    else
      DayTime.AM.toString

  def isBefore(
    wallTime: WallTime,
  ) =
    localTime.isBefore(wallTime.localTime)

  def isAfter(
    wallTime: WallTime,
  ) =
    localTime.isAfter(wallTime.localTime)

  def isBeforeOrEqualTo(
    wallTime: WallTime,
  ) =
    localTime.isBefore(wallTime.localTime) ||
    localTime.equals(wallTime.localTime)

  def isAfterOrEqualTo(
    wallTime: WallTime,
  ) =
    localTime.isAfter(wallTime.localTime) ||
    localTime.equals(wallTime.localTime)

  def between(
    wallTime: WallTime,
  ): MinuteDuration =
    MinuteDuration
      .between(
        wallTime,
        this,
      )

  def plusMinutes(
    minutes: Int,
  ) =
    WallTime(Minutes.safe(localTime.value + minutes), hourNotation)

  def plus(
    duration: MinuteDuration,
  ) =
    plusMinutes(duration.minutes.value)

  val paddedMinutes: String =
    if (minutes < 10)
      s"0$minutes"
    else
      minutes.toString

  override val toString: String = {
    val hoursVal =
      hourNotation match {
        case HourNotation.Twelve => hours12
        case HourNotation.TwentyFour => hours24
      }
    val paddedHours =
      if (hoursVal < 10)
        s"0$hoursVal"
      else
        hoursVal
    s"$paddedHours:$paddedMinutes $dayTime"
  }

  val toEUString: String = {
    val paddedHours =
      if (hours24 < 10)
        s"0$hours24"
      else
        hours24
    s"$paddedHours:$paddedMinutes"
  }

  val toDumbAmericanString: String = {
    val paddedHours =
      if (hours12 < 10)
        s"0$hours12"
      else
        hours12
    s"$paddedHours:$paddedMinutes $dayTime"
  }

  val beginningOfMorningRoutesInHours = 60 * 4

  def isLikelyEarlyMorningRatherThanLateNight: Boolean =
    this.isAfter(
      WallTime(
        Minutes.safe(beginningOfMorningRoutesInHours),
        hourNotation
      ),
    )

  def canEqual(
    other: Any,
  ): Boolean = other.isInstanceOf[WallTime]

  override def equals(
    other: Any,
  ): Boolean = other match {
    case that: WallTime =>
      (that.canEqual(this)) &&
      localTime == that.localTime
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(localTime)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object WallTime {
  def apply(
    raw: String,
  ): WallTime =
    WallTime(Minutes.safe(parseMinutes(raw)),
      if(raw.contains("AM") || raw.contains("PM"))
        HourNotation.Twelve else
      HourNotation.TwentyFour
    )

  def parseMinutes(
    raw: String,
  ) = {
    if (raw.length == 5) {
      val Array(hours, minutes) = raw.split(":")
      hours.toInt * 60 + minutes.toInt
    } else if (raw.length == 8) {
      val hourOffset =
        if(raw.endsWith("AM")) 0
        else if( raw.endsWith("PM")) 12
        else throw new IllegalArgumentException(raw)
      val Array(hours, minutes) = raw.dropRight(2).trim.split(":")
      (hours.toInt + hourOffset) * 60 + minutes.toInt
    } else throw new IllegalArgumentException(raw)
  }

  // TODO Should I move this to the bus project?
  implicit val ordering: Ordering[WallTime] =
    (x: WallTime, y: WallTime) =>
      if ((x.isLikelyEarlyMorningRatherThanLateNight
          && y.isLikelyEarlyMorningRatherThanLateNight)
          || (
            !x.isLikelyEarlyMorningRatherThanLateNight
            && !y.isLikelyEarlyMorningRatherThanLateNight
          ))
        x.localTime.value.compareTo(y.localTime.value)
      else if (x.isLikelyEarlyMorningRatherThanLateNight)
        -1
      else
        1

}
sealed private trait DayTime

private object DayTime {
  case object AM extends DayTime
  case object PM extends DayTime
}
