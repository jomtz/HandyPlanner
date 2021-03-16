package com.josmartinez.handyplanner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class TaskFragment : Fragment() {

    private lateinit var task : Task
    private lateinit var titleField : EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        task = Task()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        titleField = view.findViewById(R.id.task_title) as EditText
        dateButton = view.findViewById(R.id.task_date) as Button
        solvedCheckBox = view.findViewById(R.id.task_completed) as CheckBox

        dateButton.apply {
            text = task.date.toString()
            isEnabled = false
        }
        return view
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

        solvedCheckBox.apply {
            setOnCheckedChangeListener{_, isChecked ->
                task.isCompleted = isChecked
            }
        }

    }
}