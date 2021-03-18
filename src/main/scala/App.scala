import com.billding.time.{BusTime, TimeShit}
import com.raquo.laminar.api.L
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom
import org.scalajs.dom.html

object App {
  import com.raquo.laminar.api.L._
  def main(args: Array[String]): Unit = {
    documentEvents.onDomContentLoaded.foreach { _ =>
//      val (pickedTime: L.Signal[BusTime], timePicker: Div) =
//        TimePicker.TimePicker(BusTime("7:20"))
      println("doing stuff?")
      val appContainer = dom.document.querySelector("#laminarApp")
      val appElement =
        div(
          h1("Hello world!"),
//          child.text <-- pickedTime.map(_.toDumbAmericanString),
          TimeShit.body
//          timePicker
        )
      render(appContainer, appElement)
    }(unsafeWindowOwner)
  }
}
