package ge.nika.preconditions.core.api.precondition

import ge.nika.preconditions.core.precondition.*
import ge.nika.preconditions.core.precondition.And
import ge.nika.preconditions.core.precondition.Is
import ge.nika.preconditions.core.precondition.IsGreater
import ge.nika.preconditions.core.precondition.Or
import ge.nika.preconditions.core.precondition.Or.Companion.or

val andTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)

    when {
        it.parameters.all { p -> p is Precondition } ->
            And.ofPreconditions(it.parameters[0] as Precondition, it.parameters[1] as Precondition)
        it.parameters.all { p -> p is Boolean } ->
            And.ofBooleans(it.parameters[0] as Boolean, it.parameters[1] as Boolean)
        else -> error("Type of both parameters of AND precondition must be either precondition or boolean")
    }
}

val orTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)

    when {
        it.parameters.all { p -> p is Precondition } ->
            Or.ofPreconditions(it.parameters[0] as Precondition, it.parameters[1] as Precondition)
        it.parameters.all { p -> p is Boolean } ->
            Or.ofBooleans(it.parameters[0] as Boolean, it.parameters[1] as Boolean)
        else -> error("Type of both parameters of OR precondition must be either precondition or boolean")
    }
}

val isTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)
    Is(it.parameters[0], it.parameters[1],)
}

val isGreaterTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)
        .verifyAllParameterAreNumbers()

    IsGreater(it.parameters[0] as Number, it.parameters[1] as Number)
}

val isLessTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)
        .verifyAllParameterAreNumbers()

    IsLess(it.parameters[0] as Number, it.parameters[1] as Number)
}

val isGreaterOrEqualTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)
        .verifyAllParameterAreNumbers()
    val (first, second) = it.parameters.map { p -> p as Number }
    IsGreater(first, second).or(Is(first, second))
}

val isLessOrEqualTranslator = PreconditionTranslator {
    it.verifyParameterCount(2)
        .verifyAllParameterAreNumbers()
    val (first, second) = it.parameters.map { p -> p as Number }
    IsLess(first, second).or(Is(first, second))
}

private fun PreconditionDescription.verifyParameterCount(expectedNumber: Int): PreconditionDescription {
    check(parameters.size == expectedNumber) {
        "$preconditionName precondition must have $expectedNumber parameters. ${parameters.size} provided"
    }
    return this
}

private fun PreconditionDescription.verifyAllParameterAreNumbers(): PreconditionDescription {
    check(parameters.all { p -> p is Number }) {
        "Both parameters of $preconditionName precondition should be numbers"
    }
    return this
}
