package ge.nika.preconditions.translate

import ge.nika.preconditions.precondition.*
import ge.nika.preconditions.statement.PreconditionDescription

val andTranslator = PreconditionTranslator {

    it.verifyParameterNumber(2)

    val firstParam = it.parameters[0]
    val secondParam = it.parameters[1]

    check(firstParam is Precondition && secondParam is Precondition) {
        "Both parameters of ${it.preconditionName} precondition should be preconditions"
    }
    And(firstParam, secondParam)
}

val isTranslator = PreconditionTranslator {
    it.verifyParameterNumber(2)
    Is(
        it.parameters[0],
        it.parameters[1],
    )
}

val isGreaterTranslator = PreconditionTranslator {
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