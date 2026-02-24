package dev.rnett.inspekt.example

import dev.rnett.inspekt.inspekt

class Greeter(val name: String) {
    fun greet() = "Hello, $name!"
}

fun main() {

    val greeter = Greeter("World")

    // Using Inspekt for compile-time reflection
    val greeterClass = inspekt(Greeter::class)

    println("Class name: ${greeterClass.name}")

    val greetMethod = greeterClass.function("greet")
    val result = greetMethod.invoke {
        dispatchReceiver = greeter
    }

    println("Result of greet(): $result")
    println("Class inspektion:\n$greeterClass")
}