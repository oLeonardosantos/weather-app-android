package br.com.weatherapp.main

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.weatherapp.R
import br.com.weatherapp.api.WeatherApi
import br.com.weatherapp.local.SharedPreferencesManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesManager = lazy { SharedPreferencesManager(this) }
    private val weatherAPI = WeatherApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.Main) {

            try {
                val cityName = sharedPreferencesManager.value.getCityName()
                println("--- ✅ --- City saved: $cityName")

                val locale = Locale.getDefault().language

                val result = weatherAPI.create().getCurrentWeather(location = cityName, lang = locale)
                println("Resultado da API agora so ir colocando ma tela os dados :: $result")

                val city = findViewById<TextView>(R.id.city)
                city.text = cityName

                val state = findViewById<TextView>(R.id.state)
                state.text = result.location.timezoneId

                val celsius = findViewById<TextView>(R.id.celsius)
                celsius.text = result.current.tempCelsius.toInt().toString()

                val skyCondition = findViewById<TextView>(R.id.skyCondition)
                skyCondition.text = result.current.condition.text

                val localtime = findViewById<TextView>(R.id.localtime)
                localtime.text = formatToBrazilianFormat(result.location.localtime)

                val image = findViewById<ImageView>(R.id.image)
                Picasso.get().load(result.current.condition.getImage()).into(image)

                val umidityResult = findViewById<TextView>(R.id.umidityResult)
                umidityResult.text = result.current.humidity.toString() + "%"

                val cloudResult = findViewById<TextView>(R.id.cloudResult)
                cloudResult.text = result.current.cloud.toString() + "%"

                val country = findViewById<TextView>(R.id.country)
                country.text = result.location.country

            }catch (ex : Exception){
                showNoInternetDialog(this@MainActivity)
            }
        }
    }

    fun formatToBrazilianFormat(localtime: String): String {
        // Define the input format
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        // Parse the input string into a Date object
        val date = inputFormatter.parse(localtime)

        // Define the output format for Brazilian format
        val outputFormatter = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale("pt", "BR"))

        // Format the Date object into the desired output format
        return outputFormatter.format(date!!)
    }

    fun showNoInternetDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No Internet Connection")
        builder.setMessage("It looks like you don't have an internet connection. The app will close.")

        // Set the positive button for the user to confirm
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Close the app when the user clicks "OK"
            finish()
        }

        // Make the dialog non-cancelable (user must click OK)
        builder.setCancelable(false)

        // Create and show the dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }
}