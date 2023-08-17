package com.be_v2

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.opencsv.CSVReader
import java.io.InputStreamReader


class QuotesModel : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TextAdapterMain
    private lateinit var filePathCSV: String

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes_model)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        window.statusBarColor = Color.BLACK


        val homeBtn = findViewById<ImageView>(R.id.iv_home)
        homeBtn.setOnClickListener {
            if (this@QuotesModel.isTaskRoot) {
                val intent = Intent(this@QuotesModel, MainActivity::class.java)
                startActivity(intent)
            } else {
                this@QuotesModel.onBackPressedDispatcher.onBackPressed()
            }
        }

        setRecyclerView()

    }

    private fun setRecyclerView() {

        filePathCSV = intent.getSerializableExtra("CSV") as String
        val myText = intent.getStringExtra("extra_notification_text")


        val textData = readCsvFile()
        val arrayCSV: Array<String> = textData.toTypedArray()


        recyclerView = findViewById(R.id.rv_quote_list)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        recyclerView.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(recyclerView)


        adapter = TextAdapterMain(arrayCSV)
        recyclerView.adapter = adapter

    }


    // Function to read and parse the CSV file
    private fun readCsvFile(): List<String> {
        val inputStream = assets.open(filePathCSV)
        val reader = CSVReader(InputStreamReader(inputStream))
        val textData = mutableListOf<String>()

        var record: Array<String>?
        while (reader.readNext().also { record = it } != null) {
            record?.let {
                textData.add(it[0]) // Assuming the text is in the first column (index 0)
            }
        }

        reader.close()
        return textData
    }


}

