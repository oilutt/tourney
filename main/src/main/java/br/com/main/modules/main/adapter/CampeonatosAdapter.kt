package br.com.main.modules.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.main.R
import br.com.main.modules.main.model.Campeonato

class CampeonatosAdapter(private val list: List<Campeonato>) :
    RecyclerView.Adapter<CampeonatosAdapter.CampeonatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampeonatoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CampeonatoViewHolder(inflater.inflate(R.layout.item_campeonato, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CampeonatoViewHolder, position: Int) {

    }

    class CampeonatoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


    }
}