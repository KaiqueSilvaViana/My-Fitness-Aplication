package com.rose.myfitnesslifestyle

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonBD: Button = findViewById(R.id.button_register)

        buttonBD.setOnClickListener{
            val intent = Intent(this@MainActivity, ListResulActivity::class.java)
            intent.putExtra("type", "all")
            startActivity(intent)
        }

        val mainItem = mutableListOf<MainItem>()
        mainItem.add(
            MainItem(
                id = "IMC",
                drawableStart = R.drawable.balance_icon,
                drawableEnd = R.drawable.arrow_icon,
                stringId = R.string.imc_button,
                color = R.color.blue_light
            )
        )

        mainItem.add(
            MainItem(
                id = "TMB",
                drawableStart = R.drawable.calculate_icon,
                drawableEnd = R.drawable.arrow_icon,
                stringId = R.string.tmb_button,
                color = R.color.warm_red
            )
        )

        mainItem.add(
            MainItem(
                id = "WATER",
                drawableStart = R.drawable.bubble_icon,
                drawableEnd = R.drawable.arrow_icon,
                stringId = R.string.water_button,
                color = R.color.blue_gray
            )
        )

        val adapter = MainAdapter(mainItem) { id ->
            when(id){
                "IMC" -> {
                    val intent = Intent(this@MainActivity, ImcActivity::class.java)
                    startActivity(intent)
                }

                "TMB" -> {
                    val intent = Intent(this@MainActivity, TmbActivity::class.java)
                    startActivity(intent)
                }

               "WATER" -> {
                    val intent = Intent(this@MainActivity, WaterActivity::class.java)
                   startActivity(intent)
                }
            }

        }
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

    }

    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListinner: (String) -> Unit
    ) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                //estamos trabalhando apenas com o botão, pois nele que possui as propriedades
                val button: Button = itemView.findViewById(R.id.button_register)

                //definimos onde os desenhavies vão ficar
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    item.drawableStart,
                    0,
                    item.drawableEnd,
                    0
                )

                //estamos acessando as propriedades internas do aplicativo, ou seja, context é o proprio app
                val context = itemView.context

                //estamos definindo a cor como um objeto
                val background = ContextCompat.getColor(context, item.color)
                val drawable =
                    ContextCompat.getDrawable(context, R.drawable.button_form) as GradientDrawable
                drawable.setColor(background)
                button.background = drawable

                // button.background = shapeDrawable

                button.setText(item.stringId)

                button.setOnClickListener{
                    onItemClickListinner.invoke(item.id)
                }

            }

        }
    }

}