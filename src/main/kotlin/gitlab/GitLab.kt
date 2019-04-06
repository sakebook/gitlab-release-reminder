package gitlab
import DateSerializer
import com.uchuhimo.konf.ConfigSpec
import kotlinx.serialization.Serializable
import java.util.*

object GitLabConfig: ConfigSpec("") {
    val api by required<Api>("api")
    val projects by required<List<Project>>("projects")
}

data class Project(val id: Int, val note: String?, val remindingDay: Int)

data class Api(val host: String, val token: String?)

@Serializable
data class Tag(
    val commit: Commit,
    val message: String?,
    val name: String,
    val release: Release?,
    val target: String
)

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

@Serializable
data class Release(
    val description: String,
    val tag_name: String
)