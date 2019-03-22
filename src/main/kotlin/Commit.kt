
import kotlinx.serialization.Serializable

@Serializable
data class Commit(
    val author_email: String,
    val author_name: String,
    val authored_date: String,
    val committed_date: String,
    val committer_email: String,
    val committer_name: String,
    val created_at: String,
    val id: String,
    val message: String,
    var parent_ids: List<String>,
    val short_id: String,
    val title: String
)