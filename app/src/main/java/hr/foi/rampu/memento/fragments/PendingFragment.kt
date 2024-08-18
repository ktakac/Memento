package hr.foi.rampu.memento.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.adapters.TasksAdapter
import hr.foi.rampu.memento.helpers.MockDataLoader
import hr.foi.rampu.memento.helpers.NewTaskDialogHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PendingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendingFragment : Fragment() {

    private val mockTasks = MockDataLoader.getDemoData()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreateTask: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mockTasks.forEach { Log.i("MOCK_PENDING_TASKS", it.name)}
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_pending_tasks)
        recyclerView.adapter = TasksAdapter(MockDataLoader.getDemoData())
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        btnCreateTask = view.findViewById(R.id.fab_pending_fragment_create_task)
        btnCreateTask.setOnClickListener {
            showDialog()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PendingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PendingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showDialog() {
        val newTaskDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_task_dialog, null)

        val dialogHelper = NewTaskDialogHelper(newTaskDialogView)

        AlertDialog.Builder(context)
            .setView(newTaskDialogView)
            .setTitle(getString(R.string.create_a_new_task))
            .setPositiveButton(getString(R.string.create_a_new_task)) {_,_ ->
                val newTask = dialogHelper.buildTask()
                val tasksAdapter = (recyclerView.adapter as TasksAdapter)
                tasksAdapter.addTask(newTask)
            }
            .show()


        dialogHelper.populateSpinner(MockDataLoader.getDemoCategories())
        dialogHelper.activateDateTimeListeners()
    }
}