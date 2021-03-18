package com.billding.time

import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalTime}

import scala.util.Try

class BusTime(localTime: LocalTime) {
  val hours: Int = localTime.getHour
  val hours12: Int =
    if( hours == 0 ||  hours == 12)
      12
    else (hours % 12)
  val minutes: Int = localTime.getMinute

  def isBefore(busTime: BusTime) = {
    if (
      (this.isLikelyEarlyMorningRatherThanLateNight
        && busTime.isLikelyEarlyMorningRatherThanLateNight)
        || (
        !this.isLikelyEarlyMorningRatherThanLateNight
          && !busTime.isLikelyEarlyMorningRatherThanLateNight
        )
    ) {
      truncatedToMinutes.isBefore(busTime.truncatedToMinutes)
    } else {
      if (this.isLikelyEarlyMorningRatherThanLateNight)
        1
      else
        -1

    }


    truncatedToMinutes.isBefore(busTime.truncatedToMinutes)
  }

  def isAfter(busTime: BusTime) =
    truncatedToMinutes.isAfter(busTime.truncatedToMinutes)

  def isBeforeOrEqualTo(busTime: BusTime) =
    truncatedToMinutes.isBefore(busTime.truncatedToMinutes) ||
      truncatedToMinutes.equals(busTime.truncatedToMinutes)

  def isAfterOrEqualTo(busTime: BusTime) =
    truncatedToMinutes.isAfter(busTime.truncatedToMinutes) ||
      truncatedToMinutes.equals(busTime.truncatedToMinutes)

  private val truncatedToMinutes =
    localTime.truncatedTo(ChronoUnit.MINUTES)

  def between(busTime: BusTime): BusDuration =
    new BusDuration(
      Duration
        .between(
          busTime.truncatedToMinutes,
          truncatedToMinutes,
        )
        .abs(),
    )

  def plusMinutes(minutes: Int) =
    new BusTime(localTime.plusMinutes(minutes))

  def plus(busDuration: BusDuration) =
    new BusTime(localTime.plusMinutes(busDuration.toMinutes))

  private val dateFormat = DateTimeFormatter.ofPattern("HH:mm")

  override val toString: String = localTime.format(dateFormat)

  private val dumbAmericanDateFormat =
    DateTimeFormatter.ofPattern("h:mm a") // TODO Fuck! Why aren't you showing AM/PM???

  val toDumbAmericanString: String =
    localTime.format(dumbAmericanDateFormat) + "!"

  val isLikelyEarlyMorningRatherThanLateNight: Boolean =
    localTime.isAfter(LocalTime.parse("04:00:00"))

  def canEqual(other: Any): Boolean = other.isInstanceOf[BusTime]

  override def equals(other: Any): Boolean = other match {
    case that: BusTime =>
      (that.canEqual(this)) &&
        truncatedToMinutes == that.truncatedToMinutes
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(truncatedToMinutes)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object BusTime {
  private val dateFormat = DateTimeFormatter.ofPattern("HH:mm")

  def parseIdeal(raw: String): Try[BusTime] =
    Try(new BusTime(LocalTime.parse(raw, dateFormat)))

  def apply(raw: String) =
    parse(raw)

  def newParse(raw: String): Try[BusTime] =
    parseIdeal(raw).orElse(
      Try(new BusTime(LocalTime.parse("0" + raw)))
        .orElse(Try(new BusTime(LocalTime.parse(raw)))),
    )

  def parse(raw: String) =
    try {
      new BusTime(LocalTime.parse(raw, dateFormat))
    } catch {
      case ex: DateTimeParseException =>
        try {
          new BusTime(LocalTime.parse("0" + raw, dateFormat))
        } catch {
          case ex: DateTimeParseException =>
            new BusTime(LocalTime.parse(raw))
        }
    }

  def catchableBus(now: BusTime, goal: BusTime) =
    goal.truncatedToMinutes
      .isAfter(now.truncatedToMinutes) ||
      goal.truncatedToMinutes
        .equals(now.truncatedToMinutes)

  implicit val busTimeOrdering: Ordering[BusTime] =
    (x: BusTime, y: BusTime) => {
      if (
        (x.isLikelyEarlyMorningRatherThanLateNight
          && y.isLikelyEarlyMorningRatherThanLateNight)
          || (
          !x.isLikelyEarlyMorningRatherThanLateNight
            && !y.isLikelyEarlyMorningRatherThanLateNight
          )
      ) {
        x.truncatedToMinutes.compareTo(y.truncatedToMinutes)
      } else {
        if (x.isLikelyEarlyMorningRatherThanLateNight)
          -1
        else
          1

      }

    }
}
