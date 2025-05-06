package com.rose.myfitnesslifestyle

import android.content.Context
import android.content.Intent
import android.content.res.Resources.Theme
import android.inputmethodservice.InputMethodService
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

class ImcActivity : AppCompatActivity() {

    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_imc)

        editWeight = findViewById(R.id.edit_imc_weight)
        editHeight = findViewById(R.id.edit_imc_height)

        val btnImc: Button = findViewById(R.id.button_imc)
        btnImc.setOnClickListener{
            if(!validate()){
                Toast.makeText(this, R.string.alert, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val editweight = editWeight.text.toString().toInt()
            val editheight = editHeight.text.toString().toInt()

            val result = calcImc(editweight,editheight)
            val imcRespondeID = imcResponse(result)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.imc_response, result))
                .setMessage(imcRespondeID)
                .setPositiveButton(android.R.string.ok){ dialog, which ->

                }
                .setNegativeButton(R.string.save) {dialog, which ->
                    Thread{
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = result))

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
        val intent = Intent(this@ImcActivity, ListResulActivity::class.java)
        intent.putExtra("type", "imc")
        startActivity(intent)
    }

    private fun calcImc(weight: Int, height: Int) : Double{
        return weight / ((height / 100.0) * (height /100.0))
    }

    private fun imcResponse(imc: Double) : Int{
        return when{
            imc < 15.0 -> R.string.imc_severely_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 30.0 -> R.string.imc_high_weight
            imc < 35.0 -> R.string.imc_so_high_weight
            imc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }
    }

    private fun validate() : Boolean{
        return (editWeight.text.toString().isNotEmpty()
                &&editHeight.text.toString().isNotEmpty()
                &&!editWeight.text.toString().startsWith("0")
                &&!editHeight.text.toString().startsWith("0"))
    }

}