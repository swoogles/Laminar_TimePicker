import com.billding.time.{TimeShit, WallTime}
import com.raquo.laminar.api.L
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom
import org.scalajs.dom.html

object App {
  import com.raquo.laminar.api.L._

  def main(
    args: Array[String],
  ): Unit =

    val appElement =
      div(
        h1("Hello TimePicker."),
        TimeShit.body,
        onLoad --> Observer {
          _ => println("Loaded app")
        }
      )
    renderOnDomContentLoaded(dom.document.querySelector("#laminarApp"), appElement)
}
