package br.com.main.modules.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import br.com.main.R
import br.com.main.modules.main.model.Participante
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.item_participante.view.*

class ParticipantesAdapter(val list: List<Participante>) :
    RecyclerView.Adapter<ParticipantesAdapter.ParticipanteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipanteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ParticipanteViewHolder(inflater.inflate(R.layout.item_participante, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ParticipanteViewHolder, position: Int) {
        holder.edtNameParticipante.setHint(
            String.format(
                holder.edtNameParticipante.context.getString(R.string.hint_time), position + 1
            )
        )
        holder.edtNameParticipante.doOnTextChanged { text, _, _, _ ->
            list[position].nome = text.toString()
        }
    }

    class ParticipanteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val edtNameParticipante: TextInputEditText = view.edt_name_participante
    }
}