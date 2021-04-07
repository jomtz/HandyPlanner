package com.josmartinez.handyplanner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*
import androidx.lifecycle.Observer
import java.util.List.of

private const val TAG = "TaskFragment"
private const val ARG_TASK_ID = "task_id"
private const val DIALOG_DATE = "DialogDate"

class TaskFragment : Fragment() {

    private lateinit var task : Task
    private lateinit var titleField : EditText
    private lateinit var dateButton: Button
    private lateinit var completedCheckBox: CheckBox

    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProvider(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID =arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        titleField = view.findViewById(R.id.task_title) as EditText
        dateButton = view.findViewById(R.id.task_date) as Button
        completedCheckBox = view.findViewById(R.id.task_completed) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer { task ->
                task?.let {
                    this.task = task
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                    sequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
            ) {
                //This space intentionally left blank
            }

            override fun onTextChanged(
                    sequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int)
            {
                task.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                //This space intentionally left blank
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        completedCheckBox.apply {
            setOnCheckedChangeListener{_, isChecked ->
                task.isCompleted = isChecked
            }

            dateButton.setOnClickListener{
                DatePickerFragment.newInstance(task.date).apply {
                    show(this@TaskFragment.parentFragmentManager, DIALOG_DATE)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        taskDetailViewModel.saveTask(task)
    }

    private fun updateUI(){
        titleField.setText(task.title)
        dateButton.text = task.date.toString()
        completedCheckBox.apply {
            isChecked = task.isCompleted
            jumpDrawablesToCurrentState()
        }
    }

    companion object{
        fun newInstance(taskId: UUID): TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }

}