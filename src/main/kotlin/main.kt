import channels.*
import com.uchuhimo.konf.Config
import gitlab.*
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.ArrayListSerializer
import kotlin.reflect.KClass
import java.nio.charset.Charset
import java.util.*

@ImplicitReflectionSerializer
fun main(args: Array<String>) = runBlocking {
    val config = Config {
        addSpec(GitLabConfig)
        addSpec(SlackConfig)
    }.from.file(fileName(args))
    val api = config[GitLabConfig.api]
    val projects = config[GitLabConfig.projects]
    val client = createClient(api)

    projects.forEach { project ->
        val tags = fetchTag(client, project)
        val filteredTags = filteredTag(tags, project)
        println("Filtered tags count are ${filteredTags.size} from project id ${project.id}.")
        if (filteredTags.isEmpty()) {
            return@forEach
        }
        config.getOrNull(SlackConfig.slack)?.let {
            println("Post to Slack. Project id ${project.id}")
            postReminder(filteredTags, project, config[SlackConfig.slack])
        }
    }
}

fun postReminder(tags: List<Tag>, project: Project, channel: Channel) {
    channel.post(tags, project)
}

private fun fileName(args: Array<String>): String {
    if (args.isEmpty()) return "sample.toml"
    val fileName = args.first()
    return "/root/$fileName"
}

private fun createClient(api: Api): HttpClient {
    val tagSerializer = Tag.serializer()
    val releaseSerializer = Release.serializer()
    val commitSerializer = Commit.serializer()
    val listSerializer: KSerializer<List<Tag>> = ArrayListSerializer(tagSerializer)

    return HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(List::class as KClass<List<Tag>>, listSerializer)
                setMapper(Tag::class, tagSerializer)
                setMapper(Release::class, releaseSerializer)
                setMapper(Commit::class, commitSerializer)
            }
        }
        this.defaultRequest {
            this.header("PRIVATE-TOKEN", api.token)
            this.url { this.host = api.host }
        }
        this.engine {
            this.response.defaultCharset = Charset.forName("UTF-8")
        }
    }
}

private suspend fun fetchTag(client: HttpClient, project: Project): List<Tag> {
    return client.get("api/v4/projects/${project.id}/repository/tags")
}

private fun filteredTag(tags: List<Tag>, project: Project): List<Tag> {
    val time = Date().time
    return tags.filter {
        val hours = (time - it.commit.created_at.time) / HOUR_MILLIS
        val remindingHour = project.remindingDay * 24
        val lowerLimit = remindingHour - project.lowerLimit
        val upperLimit = remindingHour + project.upperLimit
        hours in lowerLimit..upperLimit
    }
}

const val HOUR_MILLIS = 1000 * 60 * 60
