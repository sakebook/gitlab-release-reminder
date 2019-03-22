
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    val description: String,
    val tag_name: String
)