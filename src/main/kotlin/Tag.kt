
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val commit: Commit,
    val message: String?,
    val name: String?,
    val release: Release?,
    val target: String
)