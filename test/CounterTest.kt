import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.junit.Test

class CounterTest {
    private val a = Counter()

    @Operation
    fun getAndIncrement() = a.getAndIncrement()

    @Test
    fun modelCheckingTest() = try {
        ModelCheckingOptions()
            .iterations(30)
            .invocationsPerIteration(10_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(CounterSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        log("model-checking")
        throw t
    }

    @Test
    fun stressTest() = try {
        StressOptions()
            .iterations(30)
            .invocationsPerIteration(50_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(CounterSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        log("stress")
        throw t
    }
}

class CounterSequential : VerifierState() {
    private var a = 0

    fun getAndIncrement(): Int = a++

    override fun extractState() = a
}

private fun log(strategy: String) {
    System.err.println("Failed: $strategy")
}
