import kotlinx.atomicfu.atomic

class Counter {
    private var a = 0
    private val isLocked = atomic(false)

    fun getAndIncrement(): Int {
        lock()
        val res = a++
        unlock()
        return res
    }

    private fun lock() {
        while (!isLocked.compareAndSet(false, true)) {

        }
    }

    private fun unlock() {
        isLocked.value = false
    }
}

