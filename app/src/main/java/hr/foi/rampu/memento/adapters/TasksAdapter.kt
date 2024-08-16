package hr.foi.rampu.memento.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.entities.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TasksAdapter(private val tasksList : List<Task>): RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sdf : SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.ENGLISH)

        private val taskName: TextView
        private val taskDueDate: TextView
        private val taskCategoryColor: SurfaceView

        init {
            taskName = view.findViewById(R.id.tv_task_name)
            taskDueDate = view.findViewById(R.id.tv_task_due_date)
            taskCategoryColor = view.findViewById(R.id.sv_task_category_color)
        }

        fun bind(task: Task){
            taskName.text = task.name
            taskDueDate.text = sdf.format(task.dueDate)
            taskCategoryColor.setBackgroundColor(task.category.color.toColorInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(taskView)
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }
}