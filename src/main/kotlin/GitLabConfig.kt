import com.uchuhimo.konf.ConfigSpec

object GitLabConfig: ConfigSpec("") {
    val api by required<Api>("api")
    val projects by required<List<Project>>("projects")
}

data class Project(val id: Int, val mention: String, val note: String?, val remindingDay: Int)

data class Api(val host: String, val token: String)
