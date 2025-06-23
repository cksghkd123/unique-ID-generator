package com.example.ex

import SnowflakeGenerator
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class SimpleController(private val snowflakeGenerator: SnowflakeGenerator) {
    @GetMapping("/generate-id")
    @ResponseBody
    fun generateId(): Long {
        return snowflakeGenerator.nextId()
    }
}