import scala.collection.mutable._

trait ConcurrencyTesting {

  def readValue(value: Int) = {
    Thread.sleep((Math.random() * 1000).toLong)
    value
  }

  def test(n: Int)(f: => Unit) = {
    val threads = 0.to(n).map { _ =>
      new Thread(new Runnable {
        def run() = f
      })
    }
    threads.foreach(_.start)
    threads.foreach(_.join)
  }

  class Experiment {
    val threads = new ArrayBuffer[Thread]() with SynchronizedBuffer[Thread]
    val statements = new ArrayBuffer[Symbol]() with SynchronizedBuffer[Symbol]

    def async(f: => Any): Unit = {
      val thread: Thread = new Thread(new Runnable { def run() = f })
      threads += thread
    }

    def statement(s: Symbol): Unit = {
      statements += s
      ()
    }

    def assertCondition(condition: => Boolean): Unit = {
      threads.map(_.start)
      threads.map(_.join)
      assert(condition)
      threads.clear
      statements.clear
    }

    def assertOrder(statements: Symbol*): Unit = {
      threads.map(_.start)
      threads.map(_.join)
      assert(statements.toList == this.statements.toList)
      threads.clear
      this.statements.clear
    }

    def assertOrderIsOneOf(listOfStatements: List[Symbol]*): Unit = {
      threads.map(_.start)
      threads.map(_.join)
      assert(listOfStatements contains statements.toList)
      threads.clear
      statements.clear
    }
  }
}
