import androidx.lifecycle.ViewModel
import com.josmartinez.handyplanner.Task

class TaskListViewModel : ViewModel(){

    val tasks = mutableListOf<Task>()

    init{
        for(i in 0 until 100) {
            val task = Task()
            task.title = "Task #$i"
            task.isCompleted = i % 2 == 0
            tasks += task
        }
    }
}