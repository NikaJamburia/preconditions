package ge.nika.preconditions.core.api.precondition

import ge.nika.preconditions.core.precondition.And
import ge.nika.preconditions.core.precondition.Is
import ge.nika.preconditions.core.precondition.IsGreater
import ge.nika.preconditions.core.precondition.Or
import kotlin.reflect.KClass

val andTranslator = PreconditionTranslator {

    it.verifyParameterNumber(2)

    check(
        it.parameters.all { p -> p is Precondition } ||
                it.parameters.all { p -> p is Boolean }
    ) {
        "Type of both parameters of AND precondition must be either precondition or boolean"
    }

    when {
        it.parameters.all { p -> p is Precondition } ->
            And.ofPreconditions(it.parameters[0] as Precondition, it.parameters[1] as Precondition)
        it.parameters.all { p -> p is Boolean } ->
            And.ofBooleans(it.parameters[0] as Boolean, it.parameters[1] as Boolean)
        else -> error("Type of both parameters of AND precondition must be either precondition or boolean")
    }
}

val isTranslator = PreconditionTranslator {
    it.verifyParameterNumber(2)
    Is(
        it.parameters[0],
        it.parameters[1],
    )
}

val greaterThenTranslator = PreconditionTranslator {
    it.verifyParameterNumber(2)

    val firstParam = it.parameters[0]
    val secondParam = it.parameters[1]

    check(firstParam is Number && secondParam is Number) {
        "Both parameters of ${it.preconditionName} precondition should be numbers"
    }

    IsGreater(firstParam, secondParam)
}


val orTranslator = PreconditionTranslator {
    it.verifyParameterNumber(2)

    val firstParam = it.parameters[0]
    val secondParam = it.parameters[1]

    check(firstParam is Precondition && secondParam is Precondition) {
        "Both parameters of ${it.preconditionName} precondition should be preconditions"
    }
    Or(firstParam, secondParam)
}

private fun PreconditionDescription.verifyParameterNumber(expectedNumber: Int) {
    check(parameters.size == expectedNumber) {
        "$preconditionName precondition must have $expectedNumber parameters. ${parameters.size} provided"
    }
}
