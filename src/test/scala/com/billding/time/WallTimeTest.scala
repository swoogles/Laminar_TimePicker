package com.billding.time

import utest.{TestSuite, Tests, test}

object WallTimeTest extends TestSuite {
  val tests = Tests {
    test("basic") {
      println(WallTime("08:30").minutes)
      assert(WallTime("08:30").minutes == 30)
    }

    test("am/pm parsing") {
      println(WallTime("08:30 AM").minutes)
      assert(WallTime("08:30 AM").minutes == 30)
    }
  }
}