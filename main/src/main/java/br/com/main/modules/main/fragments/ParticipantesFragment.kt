package br.com.main.modules.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.main.R
import br.com.main.modules.main.adapter.ParticipantesAdapter
import br.com.main.modules.main.viewmodels.CreateCampViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_participantes.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ParticipantesFragment : Fragment() {
    
    private val viewModel: CreateCampViewModel by sharedViewModel()
    private lateinit var participantesAdapter: ParticipantesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_participantes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setOnClickListener { activity!!.onBackPressed() }

        btn_continuar.setOnClickListener {
            viewModel.createCamp(participantesAdapter.list)
        }
        btn_decidir_depois.setOnClickListener {
            viewModel.createCamp()
        }

        participantesAdapter = ParticipantesAdapter(viewModel.participantes().value!!)

        recycler_participantes.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = participantesAdapter
        }

        viewModel.error().observe(this, Observer {
            Snackbar.make(btn_continuar, R.string.erro_save_camp, Snackbar.LENGTH_LONG)
                .show()
        })
        viewModel.loading().observe(this, Observer {
            if (it) progress_bar.visibility =
                View.VISIBLE else progress_bar.visibility = View.GONE
        })
        viewModel.sucess().observe(this, Observer {
            activity!!.finish()
        })
    }
}