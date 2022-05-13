package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.PreconditionsModule
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator


data class PreconditionsConfig(
    val definedTranslators: Map<String, PreconditionTranslator>
)

class PreconditionsConfigBuilder {
    private val modules: MutableList<PreconditionsModule> = mutableListOf()

    fun registerModule(moduleFunction: () -> PreconditionsModule) {
        val newModule = moduleFunction()

        newModule.translators().keys.forEach { preconditionName ->
            modules.forEach {
                check(!it.contains(preconditionName)) {
                    "Cant register module <${newModule.name}>: Precondition '$preconditionName' already defined in module <${it.name}>."
                }
            }
        }
        modules.add(newModule)
    }

    fun registerPrecondition(name: String, translator: PreconditionTranslator) {
        modules.forEach {
            check(!it.contains(name)) {
                "Precondition '$name' already defined in module <${it.name}>."
            }
        }
        modules.add(customModule {
            this.name = name
            name translatedBy translator }
        )
    }

    fun build(): PreconditionsConfig = PreconditionsConfig(
        definedTranslators = modules.map { it.translators() }
            .flatMap { translators ->
                translators.map { it.toPair() }
            }.toMap()
    )

    private fun PreconditionsModule.contains(preconditionName: String): Boolean =
        this.translators().keys.contains(preconditionName)

}

fun configurePreconditions(builderFunction: PreconditionsConfigBuilder.() -> Unit): PreconditionsConfig {
    return with(PreconditionsConfigBuilder()) {
        builderFunction()
        build()
    }
}

