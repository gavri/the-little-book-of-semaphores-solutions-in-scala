import java.util.concurrent.Semaphore

object Solution33 extends ConcurrencyTesting with App {
  test(100) {
    val aSem = new Semaphore(0)
    val bSem = new Semaphore(0)
    val e = new Experiment()
    e.async {
      e.statement('a1)
      aSem.release
      bSem.acquire
      e.statement('a2)
    }

    e.async {
      e.statement('b1)
      bSem.release
      aSem.acquire
      e.statement('b2)
    }

    e.assertOrderIsOneOf(List('a1, 'b1, 'a2, 'b2),
      List('b1, 'a1, 'a2, 'b2),
      List('a1, 'b1, 'b2, 'a2),
      List('b1, 'a1, 'b2, 'a2))
  }
}
