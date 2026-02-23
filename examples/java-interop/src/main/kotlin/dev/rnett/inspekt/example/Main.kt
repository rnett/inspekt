package dev.rnett.inspekt.example

import dev.rnett.inspekt.inspekt

fun main() {
    val greeter = JavaGreeter("World")

    // Using Inspekt on a Java class
    val greeterClass = inspekt(JavaGreeter::class)

    println("Class name: ${greeterClass.name}")

    val greetMethod = greeterClass.function("greet")
    val result = greetMethod.invoke {
        dispatchReceiver = greeter
    }

    println("Result of greet(): $result")

    val nameMethod = greeterClass.function("getName")
    val nameValue = nameMethod.invoke {
        dispatchReceiver = greeter
    }
    println("Result of getName(): $nameValue")
}
