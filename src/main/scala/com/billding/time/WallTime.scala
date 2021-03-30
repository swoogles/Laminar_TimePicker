package com.billding.time

case class Minutes(
  value: Int)

case class ModMinutes private (
  m: Minutes) {

  def /(
    other: Int,
  ) = m.value / other

  def isBefore(
    other: ModMinutes,
  ): Boolean = m.value < other.m.value

  def isAfter(
    other: ModMinutes,
  ): Boolean = m.value > other.m.value
}

object ModMinutes {

  def safe(
    minutes: Minutes,
  ): ModMinutes =
    ModMinutes(Minutes(minutes.value % (24 * 60)))
}

case class WallTime private(
  localTime: ModMinutes /*numMinutes*/) {
  val hours24: Int = localTime / 60

  val hours12: Int =
    if (hours24 == 0 || hours24 == 12)
      12
    else hours24 % 12

  val minutes = localTime.m.value % 60

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
    WallTime(ModMinutes.safe(Minutes(localTime.m.value + minutes)))

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

  val toDumbAmericanString: String =
    toString

  val beginningOfMorningRoutesInHours = 60 * 4

  def isLikelyEarlyMorningRatherThanLateNight: Boolean =
    this.isAfter(
      WallTime(
        ModMinutes.safe(Minutes(beginningOfMorningRoutesInHours)),
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
    WallTime(ModMinutes.safe(Minutes(parseMinutes(raw))))

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
        x.localTime.m.value.compareTo(y.localTime.m.value)
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

