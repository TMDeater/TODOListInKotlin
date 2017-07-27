package com.example.msi.todolistinkotlin

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList

class CompletedTODO : AppCompatActivity() {
    private var backBtn: TextView? = null
    private var description: TextView? = null
    private var completedTODO: ArrayList<String>? = null
    private var arrAdapter: ArrayAdapter<String>? = null
    private var lv: ListView? = null
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_todo)
        title = "Completed TODOs"
        context = this

        backBtn = findViewById(R.id.go_back_button) as TextView
        description = findViewById(R.id.description) as TextView
        lv = findViewById(R.id.todolist) as ListView

        backBtn!!.setOnClickListener { backPage() }

        completedTODO = ArrayList<String>()

        loadTODO()

        arrAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, completedTODO!!)

        lv!!.adapter = arrAdapter
        lv!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            RemoveTODO(position)
            false
        }
    }

    @Throws(IOException::class)
    private fun loadTODO() {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val file = File(path, "complete.txt")
        if (!file.exists()){
            file.createNewFile()
        }
        val br = BufferedReader(FileReader(file))
        var line : String?
        do {
            line = br.readLine()
            if (line == null)
                break
            completedTODO!!.add(line)
        } while (true)
        br.close()
    }

    @Throws(IOException::class)
    private fun RemoveTODO(position: Int) {
        completedTODO!!.removeAt(position)
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val file = File(path, "complete.txt")
        val writer = BufferedWriter(FileWriter(file, false))
        for (todo in completedTODO!!) {
            writer.write(todo+"\n")
        }
        writer.flush()
        writer.close()
        arrAdapter!!.notifyDataSetChanged()
    }

    private fun backPage() {
        val i = Intent()
        i.setClass(this, MainActivity::class.java)
        startActivity(i)
    }
}
