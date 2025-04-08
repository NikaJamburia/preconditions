package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.Test

class PreconditionDescriptionTest {

    @Test
    fun `withMetadataParam preserves values in existing metadata and adds new value`() {
        val subject = PreconditionDescription(
            parameters = emptyList(),
            preconditionName = "",
            metaData = mapOf("1" to 1)
        )

        subject.withMetadataParam("2", 2).metaData!!
            .asClue {
                it.size shouldBe 2
                it["1"] shouldBe 1
                it["2"] shouldBe 2
            }
    }

    @Test
    fun `withMetadataParam overrides value in existing metadata`() {
        val subject = PreconditionDescription(
            parameters = emptyList(),
            preconditionName = "",
            metaData = mapOf("1" to 1, "2" to 2)
        )

        subject.withMetadataParam("2", 3).metaData!!
            .asClue {
                it.size shouldBe 2
                it["1"] shouldBe 1
                it["2"] shouldBe 3
            }
    }

    @Test
    fun `getMetadataInt returns value as int and null if not present`() {
        val subject = PreconditionDescription(
            parameters = emptyList(),
            preconditionName = "",
            metaData = mapOf("1" to 1, "2" to 2)
        )
        subject.getMetadataInt("1") shouldBe 1
        subject.getMetadataInt("3") shouldBe null
    }
}