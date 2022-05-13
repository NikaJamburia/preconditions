package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.PreconditionsModule
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator

class CustomModuleBuilder {

    private val translators = mutableListOf<Pair<String, PreconditionTranslator>>()
    var name: String? = null

    infix fun String.translatedBy(translator: PreconditionTranslator) {
        check(!translators.map { it.first }.contains(this)) {
            "Translator for precondition $this already defined"
        }
        translators.add(this to translator)
    }

    fun build(): PreconditionsModule {
        return object : PreconditionsModule {
            override val name: String = this@CustomModuleBuilder.name
                ?: error("Name not defined for custom module")

            override fun translators(): Map<String, PreconditionTranslator> = translators.toMap()
        }
    }
}

fun customModule(builderFunction: CustomModuleBuilder.() -> Unit) =
    with(CustomModuleBuilder()) {
        builderFunction()
        build()
    }