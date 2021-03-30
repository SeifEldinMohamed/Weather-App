package com.seif.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val City: String = "Cairo,EG"
    val API: String = "5688c6236fceb305faf351a5a473c8ea"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()

        info_layout.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            with(dialog) {
                setTitle("About").setMessage(
                    "Developed by: Seif Eldin Mohamed\n" +
                            "seifeldinmohamed@gmail.com\n" +
                            "Version 1.0.0\n"
                )
                setPositiveButton("OK") { dialog, which ->
                }.show()
            }
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            // Showing the ProgressBar, Making the main design GONE.
            progressBar.visibility = View.VISIBLE
            MainContainer.visibility = View.GONE
            txt_Error.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$City&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )

            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(response: String?) {
            super.onPostExecute(response)

            try {
                val jsonObj = JSONObject(response)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: " +
                        SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                            Date(updatedAt * 1000)
                        )
                // get data inside main
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                // get data inside sys
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                // get windspeed data inside wind
                val windSpeed = wind.getString("speed")
                // det weather description data inside weather
                val weatherDescription = weather.getString("description")
                // get name of city and country from response jsonObject
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                // Populating extracted data into our views
                txt_address.text = address
                txt_date.text = updatedAtText
                txt_status.text = weatherDescription.capitalize()
                txt_degree.text = temp
                txt_minT.text = tempMin
                txt_maxT.text = tempMax
                txt_Vsunrise.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                txt_Vsunset.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                txt_Vwind.text = windSpeed
                txt_Vpressure.text = pressure
                txt_Vhumidity.text = humidity
                // Views populated, Hiding the progressBar, Showing the main design.
                progressBar.visibility = View.GONE
                MainContainer.visibility = View.VISIBLE

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                txt_Error.visibility = View.VISIBLE
            }
        }
    }
}