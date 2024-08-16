package hr.foi.rampu.memento.helpers

import hr.foi.rampu.memento.entities.Task
import hr.foi.rampu.memento.entities.TaskCategory
import java.util.Date

object MockDataLoader {
    fun getDemoData() : List<Task> =listOf(
        Task("Submit seminar paper", Date(), TaskCategory("RAMPU", "#000080"), false),
        Task("Prepare for exercises", Date(), TaskCategory("RPP", "#FF0000"), false),
        Task("Rally a project", Date(), TaskCategory("RAMPU", "#000080"), false),
        Task("Connect to server (SSH)", Date(), TaskCategory("RWA", "#CCCCCC"), false)
    )
}
