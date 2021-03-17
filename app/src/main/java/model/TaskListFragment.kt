import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josmartinez.handyplanner.R
import com.josmartinez.handyplanner.Task


private const val TAG = "TaskListFragment"

class TaskListFragment : Fragment() {

    private lateinit var taskRecyclerView: RecyclerView

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total tasks: ${taskListViewModel.tasks.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        taskRecyclerView =
            view.findViewById(R.id.list_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val tasks = taskListViewModel.tasks
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
            Toast.makeText(context, "${task.title} pressed!", Toast.LENGTH_SHORT).show()
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