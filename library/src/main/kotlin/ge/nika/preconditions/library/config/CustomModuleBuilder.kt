package ge.nika.preconditions.library.config

import ge.nika.preconditions.core.api.config.PreconditionsModule
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator

class CustomModuleBuilder {

    private val translators = mutableListOf<Pair<String, PreconditionTranslator>>()
    var name: String? = null

    infix fun String.translatedBy(translator: PreconditionTranslator) {
        check(nameIsUnique(this)) {
            "Translator for precondition $this already defined"
        }
        translators.add(this to translator)
    }


    infix fun String.aliasFor(preconditionName: String) {
        check(nameIsUnique(this)) {
            "Cannot register alias to module $name: $this already defined!"
        }

        val translator = translators.firstOrNull { it.first == preconditionName }?.second
        check(translator != null) {
            "Cannot register alias to module $name: precondition with name $preconditionName not found!"
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

    private fun nameIsUnique(name: String) = !translators.map { it.first }.contains(name)

}

fun customModule(name: String, builderFunction: CustomModuleBuilder.() -> Unit) =
    with(CustomModuleBuilder()) {
        this.name = name
        builderFunction()
        build()
    }