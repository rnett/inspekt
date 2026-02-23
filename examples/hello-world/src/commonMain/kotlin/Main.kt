package dev.rnett.inspekt.example

import dev.rnett.inspekt.inspekt
import kotlin.jvm.JvmStatic

class Greeter(val name: String) {
    fun greet() = "Hello, $name!"
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) {

        val greeter = Greeter("World")

        // Using Inspekt for compile-time reflection
        val greeterClass = inspekt(Greeter::class)

        println("Class name: ${greeterClass.name}")

        val greetMethod = greeterClass.function("greet")
        val result = greetMethod.invoke {
            dispatchReceiver = greeter
        }

        println("Result of greet(): $result")
    }
}
