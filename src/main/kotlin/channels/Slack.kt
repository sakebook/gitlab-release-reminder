package channels

import com.uchuhimo.konf.ConfigSpec
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import gitlab.Tag
import gitlab.Project
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.TimeZone

object SlackConfig : ConfigSpec("") {
    val slack by required<Slack>("slack")
}

data class Slack(
    val webhook: String,
    val channel: String?,
    val mention: String,
    val userName: String?,
    val iconEmoji: String?
) : Channel {

    private val df: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm").apply {
        timeZone = TimeZone.getDefault()
    }

    override fun post(tags: List<Tag>, project: Project) {
        val attachments = tags.map {
            Attachment(
                author_link = "${project.note}",
                author_name = "Release Note",
                fields = listOf(
                    Field(true, "title", it.commit.title),
                    Field(true, "commit_hash", it.commit.id),
                    Field(true, "created_at", df.format(it.commit.created_at)),
                    Field(true, "author", it.commit.author_name)
                ),
                pretext = "<@$mention>",
                text = "${it.message}",
                title = it.name
            )
        }
        val message = Message(
            attachments,
            channel = this.channel,
            icon_emoji = this.iconEmoji,
            text = "リリースしてから${project.remindingDay}日経過しました。そろそろ振り返りませんか？",
            username = this.userName
        )
        val json = Json.stringify<Message>(Message.serializer(), message)

        val client = HttpClient()
        val slackUrl = this.webhook
        runBlocking {
            client.post<Unit>(slackUrl) {
                body = json
            }
        }
    }
}

@Serializable
data class Message(
    @Optional
    val attachments: List<Attachment>? = null,
    @Optional
    val channel: String? = null,
    @Optional
    val icon_emoji: String? = null,
    val text: String,
    @Optional
    val username: String? = null
)

@Serializable
data class Attachment(
    @Optional
    val author_icon: String? = null,
    @Optional
    val author_link: String? = null,
    @Optional
    val author_name: String? = null,
    @Optional
    val color: String? = null,
    @Optional
    val fallback: String? = null,
    @Optional
    val fields: List<Field>? = null,
    @Optional
    val footer: String? = null,
    @Optional
    val footer_icon: String? = null,
    @Optional
    val image_url: String? = null,
    @Optional
    val pretext: String? = null,
    @Optional
    val text: String? = null,
    @Optional
    val thumb_url: String? = null,
    @Optional
    val title: String? = null,
    @Optional
    val title_link: String? = null,
    @Optional
    val ts: Int? = null
)

@Serializable
data class Field(
    @Optional
    val short: Boolean? = null,
    @Optional
    val title: String? = null,
    @Optional
    val value: String? = null
)