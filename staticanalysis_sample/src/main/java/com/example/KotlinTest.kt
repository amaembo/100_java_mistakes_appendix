package com.example

import kotlin.math.sqrt

fun main() {
    println(testSqrt(3.0, 4.0))
}

fun testSqrt(a: Double, b: Double) {
    println(sqrt(a * a + b * b))
}