package com.rose.myfitnesslifestyle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rose.myfitnesslifestyle.modelBD.Calc

class TmbActivity : AppCompatActivity() {

    private lateinit var lifestyle: AutoCompleteTextView
    private lateinit var editAge: EditText
    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tmb)

        lifestyle = findViewById(R.id.auto_lifestyle)
        val items = resources.getStringArray(R.array.tmb_lifestyle)
        lifestyle.setText(items.first())
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,items)
        lifestyle.setAdapter(adapter)

        editAge = findViewById(R.id.edit_tmb_age)
        editWeight = findViewById(R.id.edit_tmb_weight)
        editHeight = findViewById(R.id.edit_tmb_height)

        val buttonTmb: Button = findViewById(R.id.button_tmb)

        buttonTmb.setOnClickListener{
            if(!validate()){
                Toast.makeText(this, R.string.alert, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val age = editAge.text.toString().toInt()
            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()

            val result = calcTmb(age, weight, height)
            val tmbResponse = tmbRequest(result)

            AlertDialog.Builder(this)
                .setMessage(getString(R.string.tmb_response, tmbResponse))
                .setPositiveButton(android.R.string.ok){ dialog, which ->
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
        val intent = Intent(this@TmbActivity, ListResulActivity::class.java)
        startActivity(intent)
    }

    private fun calcTmb(age: Int, weight: Int, height: Int) : Double{
        return 88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age)
    }

    private fun tmbRequest(tmb: Double) : Double{
        val items = resources.getStringArray(R.array.tmb_lifestyle)
        return when {
            lifestyle.text.toString() == items[0] -> tmb * 1.2
            lifestyle.text.toString() == items[1] ->  tmb * 1.375
            lifestyle.text.toString() == items[2] ->  tmb * 1.55
            lifestyle.text.toString() == items[3] ->  tmb * 1.725
            lifestyle.text.toString() == items[4] ->  tmb * 1.9
            else -> 0.0
        }
    }

    private fun validate() : Boolean{
        return (editAge.text.toString().isNotEmpty()
                &&editWeight.text.toString().isNotEmpty()
                &&editHeight.text.toString().isNotEmpty()
                &&!editAge.text.toString().startsWith("0")
                &&!editWeight.text.toString().startsWith("0")
                &&!editHeight.text.toString().startsWith("0"))
    }
}