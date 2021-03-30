package com.billding.time

import utest.{TestSuite, Tests, test}


/*
object BusTimeTest extends munit.FunSuite {
  test("basic") {
    assert(BusTime("08:00").minutes == 480)
  }
}

 */

object WallTimeTest extends TestSuite {
  val tests = Tests {
    test("basic") {
      println(WallTime("08:30").minutes)
      assert(WallTime("08:30").minutes == 30)
    }
  }
}