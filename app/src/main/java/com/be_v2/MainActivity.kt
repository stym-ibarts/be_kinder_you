package com.be_v2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.be_v2.databinding.ActivityMainBinding
import com.google.android.gms.common.api.Response
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.util.Stack
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


val listCSVEnglish = arrayOf(
    "english/1Affirbe.csv",
    "english/2GoodVibesbe.csv",
    "english/3BodyBalanceb.csv",
    "english/4HealthyLifebe.csv",
    "english/5BetterRelationbe.csv",
    "english/6HappyParentbe.csv",
    "english/7Selfesteembe.csv",
    "english/8HappyFambe.csv",
    "english/9generalIMprovbe.csv",
    "english/10Relaxbe.csv"
)
val listCSVPolish = arrayOf(
    "polish/1Afirmacje.csv",
    "polish/2DobreWibracje.csv",
    "polish/3DlaCiala.csv",
    "polish/4ZdrowyStylZycia.csv",
    "polish/5LepszeZwiazki.csv",
    "polish/6SzczesliwyRodzic.csv",
    "polish/7PoczucieWlasnejWartosci.csv",
    "polish/8SzczesliwaRodzina.csv",
    "polish/9RozwojOsobisty.csv",
    "polish/10Relaks.csv"
)
var listCSV = arrayOf(
    "english/1Affirbe.csv",
    "english/2GoodVibesbe.csv",
    "english/3BodyBalanceb.csv",
    "english/4HealthyLifebe.csv",
    "english/5BetterRelationbe.csv",
    "english/6HappyParentbe.csv",
    "english/7Selfesteembe.csv",
    "english/8HappyFambe.csv",
    "english/9generalIMprovbe.csv",
    "english/10Relaxbe.csv"
)


val arrCategoryEnglish = listOf(
    mapOf("Title" to "Affirmations"),
    mapOf("Title" to "Good\nVibes"),
    mapOf("Title" to "Body\nBalance"),
    mapOf("Title" to "Healthy\nLifestyle"),
    mapOf("Title" to "Better\nRelationship"),
    mapOf("Title" to "Happy\nParent"),
    mapOf("Title" to "Self\nEsteem"),
    mapOf("Title" to "Happy\nFamily"),
    mapOf("Title" to "General\nImprovement"),
    mapOf("Title" to "Relax")
)
val arrCategoryPolish = listOf(
    mapOf("Title" to "Afirmacje"),
    mapOf("Title" to "Dobre\nWibracje"),
    mapOf("Title" to "Dla\nCiała"),
    mapOf("Title" to "Zdrowy\nStyl Życia"),
    mapOf("Title" to "Lepsze\nZwiązki"),
    mapOf("Title" to "Szczęśliwy\nRodzic"),
    mapOf("Title" to "Poczucie\nWłasnej\nWartości"),
    mapOf("Title" to "Szczęśliwa\nRodzina"),
    mapOf("Title" to "Rozwój\nOsobisty"),
    mapOf("Title" to "Relaks")
)
var arrCategory = listOf(
    mapOf("Title" to "Affirmations"),
    mapOf("Title" to "Good\nVibes"),
    mapOf("Title" to "Body\nBalance"),
    mapOf("Title" to "Healthy\nLifestyle"),
    mapOf("Title" to "Better\nRelationship"),
    mapOf("Title" to "Happy\nParent"),
    mapOf("Title" to "Self\nEsteem"),
    mapOf("Title" to "Happy\nFamily"),
    mapOf("Title" to "General\nImprovement"),
    mapOf("Title" to "Relax")
)

var firebaseToken = ""


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private val doubleClickHandler = Handler()
    private val doubleClickRunnable = Runnable {
        doubleBackToExitPressedOnce = false
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    @RequiresApi(Build.VERSION_CODES.R)
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)


        window.navigationBarColor = Color.BLACK
        window.statusBarColor = Color.BLACK

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Handle the error here
                    return@addOnCompleteListener
                }

                // Get the token
                val token = task.result

//                Log.i("token", token)
                // Use the token for sending notifications or other purposes
//                println("FCM Token: $token")
            }

        /*val client: OkHttpClient = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
        val body: RequestBody = RequestBody.create(
            mediaType,
            "{\n    \"token_id\":\"fuScJoVgv0wHrBndMsnLBl:APA91bEgaOofmrkALX-CXmrUPYsrdhfd081AKjktElQHzVPo--p99-ffsH3BAosaw9CazrykAYReFzirvyOjBKp3CLzexiEhQXyvTxbWTBszj-O7MkX41gymCuNdefZHdYw3Xw_kFm-f\" ,\n    \"lang\":\"eng\",\n    \"device_id\" : \"6666666\",\n    \"tableName\":rage:\"affirbe\"]\n}"
        )
        val request: Request = Builder()
            .url("https://bekinder.adfames.com/input_api.php")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build()
        val response: Response<*> = client.newCall(request).execute()
*/

        val sharedPreferences = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val selectedLang = sharedPreferences.getString("selectedLang", "null")
        if (selectedLang == "polish") {
            arrCategory = arrCategoryPolish
            listCSV = listCSVPolish

        } else if (selectedLang == "english") {
            arrCategory = arrCategoryEnglish
            listCSV = listCSVEnglish
        }

        val notificationTexts = arrayOf(
            getQuoteFromSelectedCategories(),
            getQuoteFromSelectedCategories(),
            getQuoteFromSelectedCategories()
        )

        NotificationHelper.scheduleNotification(this, notificationTexts)

        setRecyclerView(arrCategory)

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun requestNotificationPermission() {

            val requestPermissionLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        Toast.makeText(
                            this,
                            "you should allow notification permission to Receive Daily Quotes",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }


        }

        requestNotificationPermission()

//        sendNotification()

        val settingsBtn = findViewById<ImageView>(R.id.setting)
        settingsBtn.setOnClickListener {
            val i = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(i)
        }

    }


    private fun setRecyclerView(arrCategory: List<Map<String, String>>) {

        class CategoryAdapter(private val categories: List<Map<String, String>>) :
            RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_category, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val category = categories[position]
                val title = category["Title"]
                holder.textView.text = title

                holder.itemView.setOnClickListener {
                    when (position) {
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> {
                            val i = Intent(this@MainActivity, QuotesModel::class.java)
                            i.putExtra("CSV", listCSV[position])
                            startActivity(i)
                        }
                    }
                }
            }

            override fun getItemCount(): Int {
                return categories.size
            }

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var imageView: ImageView
                var textView: TextView

                init {
                    imageView = itemView.findViewById(R.id.imageView)
                    textView = itemView.findViewById(R.id.textView)
                }
            }
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val adapter = CategoryAdapter(arrCategory)
        recyclerView.adapter = adapter


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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        // Reset the double-click flag after a delay
        doubleClickHandler.postDelayed(doubleClickRunnable, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        doubleClickHandler.removeCallbacks(doubleClickRunnable)
    }

    override fun onResume() {
        super.onResume()

        Intent.FLAG_ACTIVITY_CLEAR_TOP

        val sharedPreferences = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val selectedLang = sharedPreferences.getString("selectedLang", "null")

        if (selectedLang == "polish") {
            arrCategory = arrCategoryPolish
            listCSV = listCSVPolish

        } else if (selectedLang == "english") {
            arrCategory = arrCategoryEnglish
            listCSV = listCSVEnglish
        }

        setRecyclerView(arrCategory)
//        sendNotification()
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
        return quote

    }

//    private fun sendNotification() {
//        coroutineScope.launch(Dispatchers.IO) {
//
//            FirebaseMessaging.getInstance().token
//                .addOnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        // Handle the error here
//                        return@addOnCompleteListener
//                    }
//                    val token = task.result
//                    firebaseToken = token
//                }
//
//                    Log.i("token", firebaseToken)
//
//            val client = OkHttpClient()
//            val mediaType = "application/json".toMediaType()
////            val body = " {\r\n  \"to\": \"  ezbQDBLkRP6WBYPRtDwkUa:APA91bGLrZxJ-02BQVQ-N1vhp-QXEteedrfdFgm3NPI_-HlVjAWNC1HfittcQIUXja-i88PE-hGiYBCXEEP_bv-qupArLXMqWcxDzFbYmxFPMqZoiB6qK0aAarRVHwo9AFctWe3-NYD4\",\r\n  \"notification\": {\r\n    \"body\": \"This is a test notification\"\r\n  }\r\n}".toRequestBody(mediaType)
//
//            val body =
//                "{\r\n    \"token_id\": \"  cjv57XvcSA-Jac8lS_cZ87:APA91bGg69xDpG9YsSWX3t747HfLxMvxh_LOsh431M35NhBlsRlcwTfXF0cb7jZDJTDVNXhRh0CsdT8jVysgTyNZOHkCjOTGkvRUKm9r2ugp_clMypxTcjQ52hXLMv3fGc3Q3VwyAF2S\",\r\n    \"lang\": \"eng\",\r\n    \"device_id\": \"dfdf\",\r\n    \"tableName\": [\r\n        \"affirbe\",\r\n        \"bodybalanceb\"\r\n    ]\r\n}".toRequestBody(
//                    mediaType
//                )
//
//
//            val request = Request.Builder()
//                .url("https://bekinder.adfames.com/input_api.php")
//                .post(body)
//                .addHeader("Content-Type", "application/json")
//                .build()
//
//            val response = client.newCall(request).execute()
//
//
//        }
//    }


}
