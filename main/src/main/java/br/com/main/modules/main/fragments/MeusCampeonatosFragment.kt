package br.com.main.modules.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.main.R
import br.com.main.modules.main.adapter.CampeonatosAdapter
import br.com.main.modules.main.viewmodels.MeusCampeonatosViewModel
import kotlinx.android.synthetic.main.fragment_meus_campeonatos.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MeusCampeonatosFragment : Fragment() {
    
    private val viewModel: MeusCampeonatosViewModel by viewModel()
    private lateinit var campeonatosAdapter: CampeonatosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_meus_campeonatos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.campeonatos().observe(this, Observer {
            campeonatosAdapter = CampeonatosAdapter(it)
            recycler_campeonatos.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = campeonatosAdapter
            }
            if (campeonatosAdapter.itemCount == 0) {
                place_holder_text_view.text = getString(R.string.nenhum_campeonato)
                place_holder_text_view.visibility = View.VISIBLE
            } else {
                place_holder_text_view.visibility = View.GONE
            }
        })

        viewModel.loading().observe(this, Observer {
            swipe_refresh.isRefreshing = it
        })

        viewModel.error().observe(this, Observer {
            place_holder_text_view.text = getString(R.string.erro_get_camps)
            place_holder_text_view.visibility = View.VISIBLE
        })

        swipe_refresh.setOnRefreshListener {
            viewModel.buscarMeusCampeonatos()
        }

        viewModel.buscarMeusCampeonatos()
    }
}