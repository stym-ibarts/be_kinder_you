package com.be_v2

import androidx.appcompat.app.AppCompatActivity
import com.opencsv.CSVReader
import java.io.InputStreamReader

class CSV_Reader :AppCompatActivity() {






    // Function to read and parse the CSV file
    private fun readCsvFile(): List<String> {
        val inputStream = assets.open("test.csv")
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