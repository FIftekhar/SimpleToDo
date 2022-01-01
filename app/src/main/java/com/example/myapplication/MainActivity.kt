package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.apache.commons.io.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var ListOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {

                // Remove item from list
                ListOfTasks.removeAt(position)

                // Notify adapter that data set has changed
                adapter.notifyDataSetChanged()

                // Save the tasks list
                saveItems()
            }
        }

        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(ListOfTasks, onLongClickListener)

        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field so tasks can be added to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button and then set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {

            // Retrieve text that user inputted
            val userInputtedTask = inputTextField.text.toString()

            // Add the text to the list
            ListOfTasks.add(userInputtedTask)

            // Notify the adapter that data has changed
            adapter.notifyItemInserted(ListOfTasks.size - 1)

            // Clear text field after item is added
            inputTextField.setText("")

            // Save tasks list to file
            saveItems()
        }
    }

    // Save the data the user inputted by reading and writing from and to a file

    // Get the file we need
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    // Load items by reading from the file
    fun loadItems() {

        try {
            ListOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    // Save the items by writing to the file
    fun saveItems() {

        try {
            FileUtils.writeLines(getDataFile(), ListOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }
}