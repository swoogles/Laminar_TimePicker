package com.billding.time

case class Minutes private(
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
  private val  max = 24 * 60

  def safe(
            value: Int,
          ): Minutes =
    Minutes(mod(value, max))

  private def mod(dividend: Int, divisor: Int): Int =
    ((dividend % divisor) + divisor) % divisor

}

case class WallTime private(
  localTime: Minutes /*numMinutes*/) {
  val hours24: Int = localTime / 60

  val hours12: Int =
    if (hours24 == 0 || hours24 == 12)
      12
    else hours24 % 12

  val minutes = localTime.value % 60

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
    WallTime(Minutes.safe(localTime.value + minutes))

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
    val paddedHours =
      if (hours12 < 10)
        s"0$hours12"
      else
        hours12
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

  val toDumbAmericanString: String =
    toString

  val beginningOfMorningRoutesInHours = 60 * 4

  def isLikelyEarlyMorningRatherThanLateNight: Boolean =
    this.isAfter(
      WallTime(
        Minutes.safe(beginningOfMorningRoutesInHours),
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
//  import scalajs.dom.experimental.intl.Intl
//
//  def apply(): BusTime =
//    Intl.DateTimeFormat()

  def apply(
    raw: String,
  ): WallTime =
    WallTime(Minutes.safe(parseMinutes(raw)))

  def parseMinutes(
    raw: String,
  ) = {
    val Array(hours, minutes) = raw.split(":")
    hours.toInt * 60 + minutes.toInt
  }

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
private sealed trait DayTime

private object DayTime {
  case object AM extends DayTime
  case object PM extends DayTime
}

