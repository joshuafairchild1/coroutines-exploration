import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) {
    println("Start") // 1

    // Start a coroutine
    launch {
        delay(1000)
        println("Hello") // 2 - suspended until thread is available (delay() within runBlocking)
    }

    runBlocking {
        delay(2000)
        println("Delayed 2 sec") // 3 - blocks the main thread allowing the first suspended coroutine to finish
    }

    Thread.sleep(2000)
    println("Stop") // 4

  suspendOneMillionCoroutines()
}

fun suspendOneMillionCoroutines() {
  // creating a list of 1 million coroutines, each that waits a second before returning an integer
  val deferred = (1..1_000_000).map {
    async {
      delay(1000)
      it
    }
  }

  // the calls to await() must be inside of a coroutine, and we are using runBlocking
  // to keep the thread alive until all of the computations are complete
  runBlocking {
    val start = System.currentTimeMillis()
    val sum = suspend1Ms(deferred.sumBy { it.await() })
    val finish = System.currentTimeMillis()
    println("Sum: $sum")
    println("Took ${finish - start}ms to wait for sum of 1 million (1 second) suspended coroutines")
  }
}

suspend fun suspend1Ms(thing: Any): Any {
  delay(1)
  return thing
}