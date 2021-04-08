package com.josmartinez.handyplanner

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


private const val TAG = "TaskListFragment"

class TaskListFragment : Fragment() {

    /**
     * Required interface to hosting activities
     */
    interface Callbacks {
        fun onTaskSelected(taskId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var taskRecyclerView: RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        taskRecyclerView = view.findViewById(R.id.list_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        taskRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskListLiveData.observe(
            viewLifecycleOwner,
            { tasks ->
                tasks?.let {
                    Log.i(TAG, "Got tasks ${tasks.size}")
                    updateUI(tasks)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task()
                taskListViewModel.addTask(task)
                callbacks?.onTaskSelected(task.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(tasks: List<Task>) {
        taskRecyclerView.adapter = TaskAdapter(tasks)
    }


    private inner class TaskHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener{

        private lateinit var task: Task

        private val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.task_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task){
            this.task = task
            titleTextView.text = task.title
            dateTextView.text = task.date.toString()
        }

        override fun onClick(v: View?) {
            callbacks?.onTaskSelected(task.id)
        }

    }

    // Task Adapter

    private inner class TaskAdapter(var tasks: List<Task>)
           : RecyclerView.Adapter<TaskHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : TaskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
            return TaskHolder(view)
        }

        override fun getItemCount() = tasks.size

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

    }


    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }

}