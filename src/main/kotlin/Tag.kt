data class Tag(
    val commit: Commit,
    val message: Any,
    val name: String,
    val release: Release,
    val target: String
)