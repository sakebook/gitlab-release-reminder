package channels

import gitlab.Tag
import gitlab.Project

interface Channel {
    fun post(tags: List<Tag>, project: Project)
}

