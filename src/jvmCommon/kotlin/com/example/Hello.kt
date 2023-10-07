package com.example

expect fun name(): String

fun main(args: Array<String>) {
    println("Hello ${name()}")
}
