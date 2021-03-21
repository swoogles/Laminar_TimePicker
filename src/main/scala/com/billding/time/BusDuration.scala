package com.billding.time

case class BusDuration(minutes: Minutes) {
  val toMinutes: Long = minutes.value

  def times(int: Int) =
    BusDuration.ofMinutes(toMinutes.toInt * int)

  def dividedBy(duration: BusDuration) =
    toMinutes / duration.toMinutes

  def canEqual(other: Any): Boolean = other.isInstanceOf[BusDuration]

  override def equals(other: Any): Boolean = other match {
    case that: BusDuration =>
      (that.canEqual(this)) &&
        toMinutes == that.toMinutes
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(toMinutes)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object BusDuration {
  def between(a: BusTime, b: BusTime) =
    BusDuration(
      Minutes(math.abs(a.localTime.m.value - b.localTime.m.value).toInt)
    )

  def ofMinutes(minutes: Int) =
    new BusDuration(Minutes(minutes))

  class DurationFriendlyInt(int: Int) {
    def minutes: BusDuration = BusDuration.ofMinutes(int)
  }

  implicit def toBusDuration(int: Int) =
    new DurationFriendlyInt(int)
}
