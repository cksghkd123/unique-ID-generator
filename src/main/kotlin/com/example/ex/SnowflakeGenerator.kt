class SnowflakeGenerator(
    private val nodeId: Long,
    private val customEpoch: Long
) {
    companion object {
        const val NODE_ID_BITS = 10
        const val SEQUENCE_BITS = 12

        const val MAX_NODE_ID = (1L shl NODE_ID_BITS) - 1
        const val MAX_SEQUENCE = (1L shl SEQUENCE_BITS) - 1
    }

    private var lastTimestamp = -1L
    private var sequence = 0L

    @Synchronized
    fun nextId(): Long {
        var currentTimestamp = getCurrentTimeStamp()

        // 시간 역행 발생 시 예외 처리
        if (currentTimestamp < lastTimestamp) {
            throw IllegalStateException("The Curious Case Of Benjamin Button")
        }

        // 동일 밀리초 처리
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) and MAX_SEQUENCE
            if (sequence == 0L) {
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            sequence = 0
        }

        lastTimestamp = currentTimestamp

        return (currentTimestamp shl (NODE_ID_BITS + SEQUENCE_BITS)) or
                (nodeId shl SEQUENCE_BITS) or
                sequence
    }

    private fun getCurrentTimeStamp() = System.currentTimeMillis() - customEpoch

    /*
    좀 더 개선할 수 있을 것 같다. 지금은 무작정 while로 기다리는 방식. + 쓰레드가 여러개가 된다면?
     */
    private fun waitNextMillis(currentTimestamp: Long): Long {
        var timestamp = getCurrentTimeStamp()
        while (timestamp <= currentTimestamp) {
            timestamp = getCurrentTimeStamp()
        }
        return timestamp
    }
}
