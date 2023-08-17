package com.be_v2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.be_v2.databinding.ActivitySettingsBinding
import com.google.gson.Gson
import com.opencsv.CSVReader
import java.io.InputStreamReader
import java.util.Stack

var tickListChecker =
    arrayOf(true, false, false, false, false, false, false, false, false, false)

val englishItems = arrayOf(
    "Affirmations",
    "Good Vibes",
    "Body Balance",
    "Healthy Lifestyle",
    "Better Relationship",
    "Happy Parent",
    "Self Esteem",
    "Happy Family",
    "General Improvement",
    "Relax"
)
val polishItems = arrayOf(
    "Afirmacje",
    "Dobre Wibracje",
    "Dla Ciała",
    "Zdrowy Styl Życia",
    "Lepsze Związki",
    "Szczęśliwy Rodzic",
    "Poczucie Własnej Wartości",
    "Szczęśliwa Rodzina",
    "Rozwój Osobisty",
    "Relaks"
)
var listItems = englishItems


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        tickListChecker = loadTickListChecker()


        val sharedPreferences = getSharedPreferences("lang", Context.MODE_PRIVATE)

        val selectedLang = sharedPreferences.getString("selectedLang", "english")
        listItems = if (selectedLang == "english") englishItems else polishItems


        setRecyclerView(listItems)
        initViews()
        initOnClickListeners(this, sharedPreferences)


    }

    private fun initViews() {

        val english = binding.tvEnglish     //  TextView English
        val polish = binding.tvPolish       //  TextView Polish
        val black = ContextCompat.getColor(this, R.color.black)
        val white = ContextCompat.getColor(this, R.color.white)
        val background = ResourcesCompat.getDrawable(resources, R.drawable.highlight_lang, null)

        val sharedPreferences = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val lang = sharedPreferences.getString("selectedLang", "english")

        if (lang == "english") {

            polish.background = null
            polish.setTextColor(black)
            english.setTextColor(white)
            english.background = background
            listCSV = listCSVEnglish


        } else if (lang == "polish") {

            english.background = null
            polish.setTextColor(white)
            english.setTextColor(black)
            polish.background = background

        }

    }

    private fun setRecyclerView(title: Array<String>) {

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        data class ItemsViewModel(val image: Int, val text: String)
        class CustomAdapter(private val mList: List<ItemsViewModel>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

            // create new views
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                // inflates the card_view_design view
                // that is used to hold list item
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_view_design, parent, false)

                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val itemsViewModel = mList[position]
                holder.imageView.setImageResource(itemsViewModel.image)
                holder.textView.text = itemsViewModel.text

                val imageView = holder.imageView
                if (position < tickListChecker.size && !tickListChecker[position])
                    imageView.visibility = View.GONE
                var isImageVisible = tickListChecker[position]

                holder.itemView.setOnClickListener {
//                    shareTextOnly(itemsViewModel.text)

                    val mPrefs1 = getPreferences(MODE_PRIVATE)
                    val prefsEditor = mPrefs1.edit()
                    isImageVisible = !isImageVisible
                    if (isImageVisible) {

                        imageView.visibility = View.VISIBLE
                        tickListChecker[position] = true
                        val json1 = Gson().toJson(tickListChecker)
                        prefsEditor.putString("MyObject", json1)
                        prefsEditor.apply()


                    } else {

                        imageView.visibility = View.GONE
                        tickListChecker[position] = false
                        val json1 = Gson().toJson(tickListChecker)
                        prefsEditor.putString("MyObject", json1).apply()

//                      ensures at least one item is always selected.
                        var hasSelectedItems = false
                        for (isSelected in tickListChecker) {
                            if (isSelected) {
                                hasSelectedItems = true
                                break
                            }
                        }
                        if (!hasSelectedItems) {
                            tickListChecker[0] = true
                            val json = Gson().toJson(tickListChecker)
                            prefsEditor.putString("MyObject", json).apply()
                            setRecyclerView(listItems)
                            Toast.makeText(
                                this@SettingsActivity,
                                "One category must be always selected.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    tickListChecker = loadTickListChecker()
                    Log.i("tickCheck", tickListChecker.joinToString("\t"))

                }


            }

            override fun getItemCount(): Int {
                return mList.size
            }

            // Holds the views for adding it to image and text
            inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
                val imageView: ImageView = itemView.findViewById(R.id.imageview)
                val textView: TextView = itemView.findViewById(R.id.textView)
            }
        }

        val data = ArrayList<ItemsViewModel>()
        for (i in 0..9)
            data.add(ItemsViewModel(R.drawable.greentick, title[i]))
        val adapter = CustomAdapter(data)
        recyclerview.adapter = adapter

    }

    private fun initOnClickListeners(context: Context, sharedPreferences: SharedPreferences) {

        val homeButton = binding.backToHome
        val english = binding.tvEnglish     //  TextView English
        val polish = binding.tvPolish       //  TextView Polish
        val black = ContextCompat.getColor(this, R.color.black)
        val white = ContextCompat.getColor(this, R.color.white)
        val background = ResourcesCompat.getDrawable(resources, R.drawable.highlight_lang, null)


        english.setOnClickListener {
//          This part will handle change in the UI when language is changed.
//  ---------------------------------------------------------------------------
            polish.background = null
            polish.setTextColor(black)
            english.setTextColor(white)
            english.background = background
//  ---------------------------------------------------------------------------

            listItems = englishItems
            this.setRecyclerView(listItems)
            sharedPreferences.edit {
                putString("selectedLang", "english")
                apply()
            }

        }

        polish.setOnClickListener {

//       This part will handle change in the UI when language is changed.
//----------------------------------------------------------------------------------
            english.background = null
            polish.setTextColor(white)
            english.setTextColor(black)
            polish.background = background
//----------------------------------------------------------------------------------
            listItems = polishItems
            this.setRecyclerView(listItems)

            sharedPreferences.edit {
                putString("selectedLang", "polish")
                apply()
            }
        }

        homeButton.setOnClickListener {
            this@SettingsActivity.onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun loadTickListChecker(): Array<Boolean> {
        val mPrefs = getPreferences(MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString("MyObject", null)
        return gson.fromJson(json, Array<Boolean>::class.java) ?: arrayOf(
            true, false, false, false, false, false, false, false, false, false
        )
        //  prefsEditor.putString("$tickListChecker[position]", json1)
    }

    override fun onResume() {
        super.onResume()
        initViews()
        getQuoteFromSelectedCategories()
    }

    private fun shareTextOnly(title: String) {

        // The value which we will sending through data via
        // other applications is defined
        // via the Intent.ACTION_SEND
        val intent = Intent(Intent.ACTION_SEND)

        // setting type of data shared as text
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // Adding the text to share using putExtra
        intent.putExtra(Intent.EXTRA_TEXT, title)
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getSelectedCSVs(): Stack<String> {
        val answer = Stack<String>()
        tickListChecker.forEachIndexed { index, element ->
            if (element) answer.push(listCSVEnglish[index])
        }
        return answer
    }

    private fun getQuoteFromSelectedCategories(): String {

        val quote: String
        val selectedCSVs = getSelectedCSVs()
        val randomSelectedCSV = selectedCSVs.random()
        val quotesFromCSV = readCsvFile(randomSelectedCSV)
        quote = quotesFromCSV.random()

//        intent.putExtra("extra_notification_text", quote)
//        intent.putExtra("filepath", randomSelectedCSV)
//
//        NotificationReceiver().onReceive(this, intent)

        return quote

    }

    private fun readCsvFile(csvFilePath: String): List<String> {
        val inputStream = assets.open(csvFilePath)
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


