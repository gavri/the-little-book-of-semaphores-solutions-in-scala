import java.util.concurrent.Semaphore

object Solution31 extends ConcurrencyTesting with App {
  test(100) {
    val sem = new Semaphore(0)
    val e = new Experiment()

    e.async {
      e.statement('a1)
      sem.release
    }

    e.async {
      sem.acquire
      e.statement('b1)
    }

    e.assertOrder('a1, 'b1)
  }
}
