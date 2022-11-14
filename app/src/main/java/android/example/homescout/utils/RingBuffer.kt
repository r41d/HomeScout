package android.example.homescout.utils



class RingBuffer<T>(val size: Int) {

    private val buffer: MutableList<T> = mutableListOf<T>()
    private var insertPosition: Int = 0
    private var isBufferFull: Boolean = false

    fun first() : T {
      return buffer.first()
    }

    fun put(element: T) {

        if (!isBufferFull) {
            buffer.add(insertPosition, element)
            insertPosition++
            insertPosition %= size
            if (insertPosition == 0) {
                isBufferFull = true
            }
            return
        }

        buffer[insertPosition] = element
        insertPosition++
        insertPosition %= size
    }

    fun getElements(): MutableList<T> {
        return buffer
    }

    fun clear() {
        buffer.clear()
        insertPosition = 0
        isBufferFull = false
    }
}