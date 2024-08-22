package hr.foi.rampu.memento.helpers

import hr.foi.rampu.memento.database.TasksDatabase
import hr.foi.rampu.memento.entities.Task
import hr.foi.rampu.memento.entities.TaskCategory
import java.util.Date

object MockDataLoader {


    fun loadMockData() {

        val tasksDao = TasksDatabase.getInstance().getTasksDao()
        val tasksCategoriesDao = TasksDatabase.getInstance().getTaskCategoriesDao()

        if (tasksDao.getAllTasks(false).isEmpty() &&
            tasksDao.getAllTasks(true).isEmpty() &&
            tasksCategoriesDao.getAllCategories().isEmpty()
        ) {

            val categories = arrayOf(
                TaskCategory(1, "RAMPU", "#000080"),
                TaskCategory(2, "RPP", "#FF0000"),
                TaskCategory(3, "RWA", "#CCCCCC")
            )
            tasksCategoriesDao.insertCategory(*categories)

            val dbCategories = tasksCategoriesDao.getAllCategories()

            val tasks = arrayOf(
                Task(1, "Submit seminar paper", Date(), dbCategories[0].id, false),
                Task(2, "Prepare for exercises", Date(), dbCategories[1].id, false),
                Task(3, "Rally a project", Date(), dbCategories[0].id, false),
                Task(4, "Connect to server (SSH)", Date(), dbCategories[2].id, false)
            )
            tasksDao.insertTask(*tasks)

        }
    }
}
