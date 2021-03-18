package com.billding.time

import java.time.Duration

class BusDuration(duration: Duration) {
  val toMinutes: Long = duration.toMinutes

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

  def ofMinutes(minutes: Int) =
    new BusDuration(java.time.Duration.ofMinutes(minutes))

  class DurationFriendlyInt(int: Int) {
    def minutes: BusDuration = BusDuration.ofMinutes(int)
  }

  implicit def toBusDuration(int: Int) =
    new DurationFriendlyInt(int)
}
