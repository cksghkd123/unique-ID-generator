package com.example.ex

import SnowflakeGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

@Configuration
class SnowflakeConfig {
    @Value("\${snowflake.nodeId}")
    private var nodeId: Long = 0

    @Value("\${snowflake.basedTime}")
    private lateinit var basedTime: String

    @Bean
    fun snowflakeGenerator(): SnowflakeGenerator {
        val customEpoch: Long = Instant.parse(basedTime).toEpochMilli()
        return SnowflakeGenerator(nodeId, customEpoch)
    }
}