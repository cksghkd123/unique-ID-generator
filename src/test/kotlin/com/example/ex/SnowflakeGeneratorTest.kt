package com.example.ex

import SnowflakeGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class SnowflakeGeneratorTest {
    @Test
    fun `단일서버 단일프로세스 싱글스레드`() {
        val generator = SnowflakeGenerator(1, 0)
        val requestPerThread = 10000
        val result = ConcurrentHashMap.newKeySet<Long>()

        repeat(requestPerThread) {
            result.add(generator.nextId())
        }

        assertEquals(requestPerThread, result.size, "아이디가 중복되어 생성되었음")

    }

    @Test
    fun `단일서버 단일프로세스 멀티스레드`() {
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
        assertEquals(total, result.size, "아이디가 중복되어 생성되었음")


    }

}