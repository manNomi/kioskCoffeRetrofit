package com.example.kiosckoutback.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.kiosckoutback.R

class dialog() : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val bundle = arguments
            val message = bundle!!.getString("text")
            val builder = AlertDialog.Builder(it)
            val view=inflater.inflate(R.layout.dialog,null)
            builder.setView(inflater.inflate(R.layout.dialog, null))
            builder.apply {
                setTitle("주문기록")
                setMessage(message)
                setNegativeButton("확인",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d("qwe","123")
                    }
                )
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}