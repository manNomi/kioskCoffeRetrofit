package com.example.kiosckoutback

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.dialog_insert.*

class dialogClass() : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {

        val inflater = requireActivity().layoutInflater

        val builder = AlertDialog.Builder(it)

        val view=inflater.inflate(R.layout.dialog_insert,null)
//
//        val insertBtn=view.findViewById<Button>(R.id.insert_btn)
//        val backBtn=view.findViewById<Button>(R.id.back_btn)

//        Log.d("test","버튼생김")
//        insertBtn.setOnClickListener{
//            Log.d("test","버튼눌림")
//        }
//
//        backBtn.setOnClickListener{
//            dismiss()
//        }

        builder.setView(inflater.inflate(R.layout.dialog_insert, null))
            builder.apply {
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