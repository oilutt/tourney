package br.com.main.modules.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.main.R
import br.com.main.modules.main.viewmodels.CreateCampViewModel
import kotlinx.android.synthetic.main.fragment_criar_camp.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreateCampFragment : Fragment() {
    
    private val viewModel: CreateCampViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_criar_camp, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setOnClickListener { activity!!.onBackPressed() }

        edt_camp_name.doOnTextChanged { text, _, _, _ -> viewModel.nomeCamp.value = text.toString() }
        edt_descricao.doOnTextChanged { text, _, _, _ -> viewModel.descricao.value = text.toString() }
        radio_group_tipo.setOnCheckedChangeListener { _, checkedId -> viewModel.tipoId.value = checkedId }
        radio_group_participantes.setOnCheckedChangeListener { _, checkedId -> viewModel.quantidadeParticipantesId.value = checkedId }

        btn_continuar.setOnClickListener {
            findNavController().navigate(R.id.action_createCampFragment_to_participantesFragment)
        }

        viewModel.continuarEnable.observe(this, Observer {
            btn_continuar.isEnabled = it
        })
    }
}