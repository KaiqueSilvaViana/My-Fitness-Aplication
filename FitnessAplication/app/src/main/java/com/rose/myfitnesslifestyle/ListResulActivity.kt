package com.rose.myfitnesslifestyle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rose.myfitnesslifestyle.modelBD.Calc
import java.text.SimpleDateFormat
import java.util.Locale

class ListResulActivity : AppCompatActivity(), OnClickListinner {

    private lateinit var listResulCalc: MutableList<Calc>
    private lateinit var adapter: ListResultAdapter

    private lateinit var rvList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_resul)

        listResulCalc = mutableListOf<Calc>()
        adapter = ListResultAdapter(listResulCalc,this)
        rvList = findViewById(R.id.rv_result_list)
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)

        val type = intent?.extras?.getString("type") ?: "all"

        Thread{
            val app = application as App
            val dao = app.db.calcDao()
            val response = if (type == "all"){
                dao.getAllRegisters()
            }else{
                dao.getRegisterType(type)
            }

            runOnUiThread{
                listResulCalc.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()

    }

    override fun OnClick(id: Int, type: String, calc: Calc){

        AlertDialog.Builder(this)
            .setMessage(getString(R.string.edit))
            .setPositiveButton(R.string.edit_list){ dialog, which ->

                when(type) {
                    "imc" -> {
                        val intent = Intent(this, ImcActivity::class.java)
                        intent.putExtra("updateId", id)
                        startActivity(intent)
                    }
                    "tmb" -> {
                        val intent = Intent(this, TmbActivity::class.java)
                        intent.putExtra("updateId", id)
                        startActivity(intent)
                    }

                    "water" -> {
                        val intent = Intent(this, WaterActivity::class.java)
                        intent.putExtra("updateId", id)
                        startActivity(intent)
                    }
                }
                finish()

            }
            .setNegativeButton(R.string.delete) {dialog, which ->
                Thread {
                    val app = application as App
                    val dao = app.db.calcDao()

                    val response = dao.delete(calc)

                    if (response > 0) {
                        runOnUiThread {

                            val indexToRemove = listResulCalc.indexOfFirst { it.id == id }
                            if(indexToRemove != -1){
                                listResulCalc.removeAt(indexToRemove)
                                adapter.notifyItemRemoved(indexToRemove)
                            }

                        }
                    }
                }.start()
            }
            .create()
            .show()

    }


    private inner class ListResultAdapter(
        private val listResul: MutableList<Calc>,
        private val listiner: OnClickListinner
    ) : RecyclerView.Adapter<ListResultAdapter.ListResultViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListResultViewHolder {
            val layout = layoutInflater.inflate(R.layout.item_result_list_calc, parent, false)
            return ListResultViewHolder(layout)
        }

        override fun getItemCount(): Int {
            return listResul.size
        }

        override fun onBindViewHolder(holder: ListResultViewHolder, position: Int) {
            val itemCurrent = listResul[position]
            holder.bind(itemCurrent)
        }

        private inner class ListResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(item: Calc){
                val textView: TextView = itemView.findViewById(R.id.text_history)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                val date = dateFormat.format(item.date)
                val res = item.res

                textView.text = getString(R.string.list_resul, res, date)

                textView.setOnClickListener{
                    listiner.OnClick(item.id, item.type, item)
                }

            }

        }
    }
}