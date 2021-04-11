This is just one small contribution to the Laminar ecosystem.
It is my first UI component, and it's very narrowly useful at the moment.
I just love Laminar and want to make it easier for people to experiment with it!

![](docs/LaminarTimePicker.gif)

To use:
```scala
libraryDependencies += "com.billdingsoftware" %%% "timepicker" % "0.1.7"
```

The simplest use-case is:
```scala
TimePicker("08:03 AM") match {
  case TimePicker(
    component: Div, 
    $time: Signal[WallTime]
  ) =>
    div(
      child.text <-- $time.map("time: " + _),
      component
    )
}
```
This will use AM/PM, 12 hour notation. If you prefer 24 hour notation, omit the AM/PM.

```scala
TimePicker("15:00")
```
If you want to change the default minute delta of 5 minutes, provide that value via:
```scala
TimePicker("15:00", minuteDelta = 15)
```

![](docs/TimePicker_24hourTime_customMinuteDelta.gif)

You can also drop in custom up/down button elements via:
```scala
  TimePicker(
    initialTime = "09:30",
    incrementRep = span("Inc"),
    decrementRep = span("Dec")
  )
```

TODOs:
- 24 hour display mode
- Adjustable increments. Hardcoded to 5 minutes currently.
- A hundred other things