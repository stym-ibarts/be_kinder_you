package com.be_v2

import com.opencsv.CSVReader
import java.io.InputStreamReader
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class Quote : AppCompatActivity() {
    private lateinit var filePathCSV: String

    // Function to read and parse the CSV file
    fun readCsvFile(filePathCSV: String): List<String> {
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

    fun getRandomCSV(): Array<String> {
        val randPosition = Random.nextInt(0, listCSV.size)
        filePathCSV = listCSV[randPosition]
        val textData = readCsvFile(filePathCSV)
        return textData.toTypedArray()
    }


}