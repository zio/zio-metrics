package zio.metrics

import zio.{ Task, UIO }
import com.codahale.metrics.Reservoir

trait Registry {
  val registry: Registry.Service[_, _]
}

object Registry {
  trait Service[M, R] {
    def getCurrent(): UIO[R]
    def registerCounter[L: Show](label: Label[L]): Task[M]
    def registerGauge[L: Show, A](label: Label[L], f: () => A): Task[M]
    def registerHistogram[L: Show](label: Label[L], reservoir: Reservoir): Task[M]
    def registerMeter[L: Show](label: Label[L]): Task[M]
    def registerTimer[L: Show](label: Label[L]): Task[M]
  }
}
