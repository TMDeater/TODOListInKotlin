package com.example.msi.todolistinkotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import java.io.*

class MainActivity : AppCompatActivity() {
    private var inputBox: EditText? = null
    private var addItemBtn: TextView? = null
    private var completeItemBtn: TextView? = null
    private var tutorialText: TextView? = null
    private var completedTODO: ArrayList<String>? = null
    private var todoList: ArrayList<String>? = null
    private var arrAdapter: ArrayAdapter<String>? = null
    private var lv: ListView? = null
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "TODO list"
        context = this

        inputBox = findViewById(R.id.input_box) as EditText
        addItemBtn = findViewById(R.id.add_item_button) as TextView
        completeItemBtn = findViewById(R.id.complete_item_button) as TextView
        tutorialText = findViewById(R.id.tutorial) as TextView
        lv = findViewById(R.id.todolist) as ListView

        (completeItemBtn as TextView).setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                ToCompletePage()
            }
        })

        completedTODO = ArrayList<String>()
        todoList = ArrayList<String>()

        loadCompletedTODO()
        loadTODO()

        arrAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, todoList!!)

        lv!!.adapter = arrAdapter
        lv!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            RemoveTODO(position)
            true
        }
        lv!!.onItemClickListener =AdapterView.OnItemClickListener { parent, view, position, id ->
            completedTODO!!.add(todoList!![position])
            RemoveTODO(position)
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

        (addItemBtn as TextView).setOnClickListener ( object : View.OnClickListener {
            override fun onClick(v: View?) {
                addItem()
            }
        })
    }


    @Throws(IOException::class)
    private fun loadTODO() {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val file = File(path, "todolist.txt")
        if (!file.exists()){
            file.createNewFile()
        }
        val br = BufferedReader(FileReader(file))
        var line : String?
        do {
            line = br.readLine()
            if (line == null)
                break
            todoList!!.add(line)
        } while (true)
        br.close()
    }

    @Throws(IOException::class)
    private fun loadCompletedTODO() {
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
        todoList!!.removeAt(position)
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val file = File(path, "todolist.txt")
        val writer = BufferedWriter(FileWriter(file, false))
        for (todo in todoList!!) {
            writer.write(todo)
        }
        writer.flush()
        writer.close()
        arrAdapter!!.notifyDataSetChanged()

    }

    @Throws(IOException::class)
    private fun addItem() {
        todoList!!.add(inputBox!!.text.toString())
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val file = File(path, "todolist.txt")
        val writer = BufferedWriter(FileWriter(file, false))
        for (todo in todoList!!) {
            writer.write(todo+"\n")
        }
        writer.flush()
        writer.close()
        arrAdapter!!.notifyDataSetChanged()
    }

    private fun ToCompletePage() {
        val intent = Intent(this,CompletedTODO::class.java)
        startActivity(intent)
    }

}
