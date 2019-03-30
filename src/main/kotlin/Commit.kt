
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Commit(
    val author_email: String,
    val author_name: String,
    @Serializable(with = DateSerializer::class)
    val authored_date: Date,
    @Serializable(with = DateSerializer::class)
    val committed_date: Date,
    val committer_email: String,
    val committer_name: String,
    @Serializable(with = DateSerializer::class)
    val created_at: Date,
    val id: String,
    val message: String,
    var parent_ids: List<String>,
    val short_id: String,
    val title: String
)