package com.example.ex

import SnowflakeGenerator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class SnowflakeGeneratorTest {
//    @BeforeEach
//    fun setUp() {
//        TODO("Not yet implemented")
//    }
//
//    @AfterEach
//    fun tearDown() {
//        TODO("Not yet implemented")
//    }

    @Test
    fun `동시성 환경에서 ID 생성 테스트`() {
        val generator = SnowflakeGenerator(1, 0)
        val threadCount = 100
        val requestPerThread = 1000
        val latch = CountDownLatch(threadCount)
        val executor = Executors.newFixedThreadPool(threadCount)
        val result = ConcurrentHashMap.newKeySet<Long>()

        repeat(threadCount) {
            executor.submit {
                repeat(requestPerThread) {
                    result.add(generator.nextId())
                }
                latch.countDown()
            }
        }

        latch.await()
        executor.shutdown()

        val total = threadCount * requestPerThread
        assertEquals(total, result.size, "중복 없이 ID가 생성되어야 합니다.")


    }

}