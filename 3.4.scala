import java.util.concurrent.Semaphore

object Solution34 extends ConcurrencyTesting with App {
  test(100) {
    val sem = new Semaphore(1)
    val e = new Experiment()
    var count = 0
    e.async {
      sem.acquire
      count = readValue(count + 1)
      sem.release
    }

    e.async {
      sem.acquire
      count = readValue(count + 1)
      sem.release
    }

    e.assertCondition(count == 2)
  }
}
