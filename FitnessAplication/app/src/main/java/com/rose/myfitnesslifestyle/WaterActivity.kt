package com.rose.myfitnesslifestyle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rose.myfitnesslifestyle.modelBD.Calc

class WaterActivity : AppCompatActivity() {

    private lateinit var editWeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_water)

        editWeight = findViewById(R.id.water_weight)

        val buttonWater: Button = findViewById(R.id.water_button)

        buttonWater.setOnClickListener {

            if (!validate()) {
                Toast.makeText(this, R.string.alert, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val weight = editWeight.text.toString().toInt()
            val result = calcWater(weight)

            val litros = result / 1000.0

            AlertDialog.Builder(this)
                .setTitle(R.string.water_title)
                .setMessage(getString(R.string.water_response, litros))
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                }
                .setNegativeButton(R.string.save) {dialog, which ->
                    Thread{
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "tmb", res = result))

                        runOnUiThread{
                            Toast.makeText(this,R.string.saved, Toast.LENGTH_LONG).show()
                        }
                    }.start()
                }
                .create()
                .show()


        }

        val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

    }

    private fun OpenListActivity(){
        val intent = Intent(this@WaterActivity, ListResulActivity::class.java)
        startActivity(intent)
    }

    private fun calcWater(weight: Int): Double {
        return weight * 35.0
    }

    private fun validate(): Boolean {
        return (editWeight.text.toString().isNotEmpty()
                && !editWeight.text.toString().startsWith("0"))
    }
}