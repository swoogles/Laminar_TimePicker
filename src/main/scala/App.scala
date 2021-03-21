import com.billding.time.{BusTime, TimeShit}
import com.raquo.laminar.api.L
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom
import org.scalajs.dom.html

object App {
  import com.raquo.laminar.api.L._

  def main(
    args: Array[String],
  ): Unit =
    documentEvents.onDomContentLoaded.foreach {
      _ =>
        val appElement =
          div(
            h1("Hello world!"),
            TimeShit.body,
          )
        render(dom.document.querySelector("#laminarApp"), appElement)
    }(unsafeWindowOwner)
}
