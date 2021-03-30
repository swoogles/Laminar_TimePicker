package com.billding.time

import utest.{TestSuite, Tests, test}

object WallTimeTest extends TestSuite {
  val tests = Tests {
    test("basic") {
      println(WallTime("08:30").minutes)
      assert(WallTime("08:30").minutes == 30)
    }
  }
}