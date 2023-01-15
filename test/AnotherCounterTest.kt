import org.junit.Assert
import org.junit.Test

private const val MAX_COUNT_THREADS = 64
private const val COUNT_OPERATIONS = 100_000

class AnotherCounterTest {
    @Test
    fun stressTest() {
        var countThreads = 1
        while (countThreads <= MAX_COUNT_THREADS) {
            test(countThreads, COUNT_OPERATIONS)
            countThreads *= 2
        }
    }

    private fun test(countThreads: Int, countOperations: Int) {
        val counter = Counter()
        val threads = Array(countThreads) {
            Thread {
                for (j in 0 until countOperations) {
                    counter.getAndIncrement()
                }
            }
        }
        for (i in 0 until countThreads) {
            threads[i].start()
        }

        for (i in 0 until countThreads) {
            try {
                threads[i].join()
            } catch (e: InterruptedException) {
                throw RuntimeException("failed join threads", e)
            }
        }
        val expected = countThreads * countOperations
        val actual = counter.getAndIncrement()
        println("Expected: $expected")
        println("Actual: $actual")
        Assert.assertEquals(expected, actual)
    }
}