package com.billding.time

import utest.{TestSuite, Tests, test}

object WallTimeTest extends TestSuite {
  val tests = Tests {
    test("basic") {
      println(WallTime("08:30").minutes)
      assert(WallTime("08:30").minutes == 30)
      assert(WallTime("21:30").hours == 21)
    }

    test("am/pm parsing") {
      assert(WallTime("08:30 AM").minutes == 30)
      assert(WallTime("08:30 AM").hours == 8)
      assert(WallTime("08:30 PM").hours == 8)
    }
  }
}