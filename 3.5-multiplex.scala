import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger

object Solution35 extends ConcurrencyTesting with App {
  test(100) {
    val multiplexMax = 5
    val numberOfThreads = 10
    val sem = new Semaphore(multiplexMax)
    val e = new Experiment()
    var count = 0
    @volatile var maximumNumberOfThreadsInCriticalSection = 0
    val threadCount = new AtomicInteger(0)
    0.until(numberOfThreads).map { _ =>
      e.async {
        sem.acquire
        maximumNumberOfThreadsInCriticalSection = math.max(maximumNumberOfThreadsInCriticalSection, threadCount.incrementAndGet)
        Thread.sleep((Math.random() * 1000).toLong)
        threadCount.decrementAndGet()
        sem.release
      }
    }
    e.assertCondition(maximumNumberOfThreadsInCriticalSection <= multiplexMax)
  }
}
