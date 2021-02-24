package com.marwaeltayeb.currencyexchange.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.marwaeltayeb.currencyexchange.R

class DialogManager {

    companion object {
        fun showCustomAlertDialog(context: Context?, callback: DialogCallback) {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.custom_dialog)

            val listView: ListView = dialog.findViewById(R.id.lst_codes)
            val codeAdapter: ListAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Code.getCurrencyCodes(context))
            listView.adapter = codeAdapter

            listView.setOnItemClickListener { _, _, myItemInt, _ ->
                callback.onCallback(listView,myItemInt)
                dialog.cancel()
            }
            dialog.show()
        }
    }
}