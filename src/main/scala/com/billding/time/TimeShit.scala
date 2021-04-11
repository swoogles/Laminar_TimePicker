package com.billding.time

object TimeShit {

  import com.raquo.laminar.api.L._

  private def fancyUpArrowThatShouldBeProvidedByEndUser() =
    img(
      cls := "glyphicon",
      src := "/src/main/resources/icons/glyphicons-basic-222-chevron-up.svg",
    )

  private def fancyDownArrowThatShouldBeProvidedByEndUser() =
    img(
      cls := "glyphicon",
      src := "/src/main/resources/icons/glyphicons-basic-221-chevron-down.svg",
    )

  private def basicIncVerbiage() =
    span("Inc", styleAttr("font-size: 3vmin"))

  private def basicDownVerbiage() =
    span("Dec", styleAttr("font-size: 3vmin"))

  val body: Div =
    div(
      TimePicker("15:00", minuteDelta = 15) match {
        case TimePicker(component: Div, time: Signal[WallTime]) =>
          div(
            child.text <-- time.map("Exposed time: " + _),
            component,
          )
      },
      TimePicker
        (
          "15:00",
          fancyUpArrowThatShouldBeProvidedByEndUser(),
          fancyDownArrowThatShouldBeProvidedByEndUser(),
          x => x,
          15
        )
        .component,
      TimePicker
        ("08:45 AM",
                          basicIncVerbiage(),
                          basicDownVerbiage())
        .component,
    )
}
