package dev.rnett.inspekt.example

import dev.rnett.inspekt.inspekt

class Example(val message: String) {
    fun greet() {
        println(message)
    }
}

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Inspekt GraalVM Example")
        val exampleClass = inspekt(Example::class)
        println("Class name: ${exampleClass.shortName}")

        val constructor = exampleClass.primaryConstructor!!
        val instance = constructor.invoke {
            value("Hello from GraalVM!")
        }

        println("Invoking greet via reflection:")
        exampleClass.function("greet").invoke {
            dispatchReceiver = instance
        }
        println("Class inspektion:\n$exampleClass")
    }
}
