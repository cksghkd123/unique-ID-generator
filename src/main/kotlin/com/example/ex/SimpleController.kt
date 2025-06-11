package com.example.ex

import SnowflakeGenerator
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SimpleController(private val snowflakeGenerator: SnowflakeGenerator) {
    @GetMapping("/generate-id")
    fun generateId(): Long {
        return val id = snowflakeGenerator.nextId()
    }

}