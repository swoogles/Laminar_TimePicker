package animus

import com.raquo.laminar.api.L.*

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.Random

trait Component {
  def body: HtmlElement
}

object Component {
  implicit def toLaminarElement(component: Component): HtmlElement = component.body
}

case class AnimatedTicker($count: Signal[List[String]]) extends Component {
  private def indexer[A] (value1: List[A]) =
    value1.reverse.zipWithIndex.reverse

//  $count.compose(Signal.from)
  val $segments: Signal[List[(String, Int)]] =
    $count.map(indexer)

  override def body: HtmlElement =
    div(
      display.flex,
      // I have no idea what's going on in the next 5 lines
      children <-- $segments.splitTransition(_._2) { case (_, _, signal, t0) =>
        div(
          children <-- signal
            .map{ case (x, y) => (x, Random.nextInt())  }
            .splitOneTransition(identity) { case (_, (int, ignoredRandomIdentity), _, t1) =>
              div(
                int,
                t0.opacity,
                t0.width,
                t1.height
              )
            }
        )
      }
    )
}
