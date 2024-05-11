package com.example.todo


import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.todo.OnDialogCloseListener



import com.example.todo.model.ToDoModel
import com.example.todo.utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class AddNewTask : BottomSheetDialogFragment() {
    //widgets
    private var mEditText: EditText? = null
    private var mSaveButton: Button? = null
    private var myDb: DataBaseHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_newtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEditText = view.findViewById<EditText>(R.id.edittext)
        mSaveButton = view.findViewById<Button>(R.id.button_save)
        myDb = DataBaseHelper(requireActivity())
        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            mEditText?.setText(task)
            if (task!!.length > 0) {
                mSaveButton?.setEnabled(false)
            }
        }
        mEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    mSaveButton?.setEnabled(false)
                    mSaveButton?.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveButton?.setEnabled(true)
                    mSaveButton?.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val finalIsUpdate = isUpdate
        mSaveButton?.setOnClickListener(View.OnClickListener {
            val text = mEditText?.getText().toString()
            if (finalIsUpdate) {
                myDb!!.updateTask(bundle!!.getInt("id"), text)
            } else {
                val item = ToDoModel()
                item.task = text
                item.status = 0
                myDb!!.insertTask(item)
            }
            dismiss()
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is OnDialogCloseListener) {
            (activity as OnDialogCloseListener).onDialogClose(dialog)
        }
    }

    companion object {
        const val TAG = "AddNewTask"
        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }
}