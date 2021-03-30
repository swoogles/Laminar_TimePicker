package com.billding.time

case class MinuteDuration(
  minutes: Minutes) {
  val toMinutes: Long = minutes.value

  def times(
    int: Int,
  ) =
    MinuteDuration.ofMinutes(toMinutes.toInt * int)

  def dividedBy(
                 duration: MinuteDuration,
  ) =
    toMinutes / duration.toMinutes

  def canEqual(
    other: Any,
  ): Boolean = other.isInstanceOf[MinuteDuration]

  override def equals(
    other: Any,
  ): Boolean = other match {
    case that: MinuteDuration =>
      (that.canEqual(this)) &&
      toMinutes == that.toMinutes
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(toMinutes)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object MinuteDuration {

  def between(
               a: WallTime,
               b: WallTime,
  ) =
    MinuteDuration(
      Minutes(
        math.abs(a.localTime.m.value - b.localTime.m.value).toInt,
      ),
    )

  def ofMinutes(
    minutes: Int,
  ) =
    new MinuteDuration(Minutes(minutes))

  class DurationFriendlyInt(
    int: Int) {
    def minutes: MinuteDuration = MinuteDuration.ofMinutes(int)
  }

  implicit def toBusDuration(
    int: Int,
  ) =
    new DurationFriendlyInt(int)
}
