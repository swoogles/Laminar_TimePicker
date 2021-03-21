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

case class BusTime private (
  localTime: ModMinutes /*numMinutes*/) {
  println("making a bus time")
  val hours: Int = localTime / 60

  val hours12: Int =
    if (hours == 0 || hours == 12)
      12
    else hours % 12

  val minutes = localTime.m.value % 60

  val dayTime =
    if (hours > 11)
      DayTime.PM
    else
      DayTime.AM

  def isBefore(
    busTime: BusTime,
  ) =
    localTime.isBefore(busTime.localTime)

  def isAfter(
    busTime: BusTime,
  ) =
    localTime.isAfter(busTime.localTime)

  def isBeforeOrEqualTo(
    busTime: BusTime,
  ) =
    localTime.isBefore(busTime.localTime) ||
    localTime.equals(busTime.localTime)

  def isAfterOrEqualTo(
    busTime: BusTime,
  ) =
    localTime.isAfter(busTime.localTime) ||
    localTime.equals(busTime.localTime)

  def between(
    busTime: BusTime,
  ): BusDuration =
    BusDuration
      .between(
        busTime,
        this,
      )

  def plusMinutes(
    minutes: Int,
  ) =
    BusTime(ModMinutes.safe(Minutes(localTime.m.value + minutes)))

  def plus(
    busDuration: BusDuration,
  ) =
    plusMinutes(busDuration.minutes.value)

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
      BusTime(
        ModMinutes.safe(Minutes(beginningOfMorningRoutesInHours)),
      ),
    )

  def canEqual(
    other: Any,
  ): Boolean = other.isInstanceOf[BusTime]

  override def equals(
    other: Any,
  ): Boolean = other match {
    case that: BusTime =>
      (that.canEqual(this)) &&
      localTime == that.localTime
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(localTime)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object BusTime {

  def apply(
    raw: String,
  ): BusTime =
    BusTime(ModMinutes.safe(Minutes(parseMinutes(raw))))

  def parseMinutes(
    raw: String,
  ) = {
    val Array(hours, minutes) = raw.split(":")
    hours.toInt * 60 + minutes.toInt
  }

  // TODO Shouldn't be part of this class
  def catchableBus(
    now: BusTime,
    goal: BusTime,
  ) =
    goal.localTime
      .isAfter(now.localTime) ||
    goal.localTime
      .equals(now.localTime)

  implicit val busTimeOrdering: Ordering[BusTime] =
    (x: BusTime, y: BusTime) =>
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
