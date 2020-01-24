package br.com.main.modules.main.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import br.com.main.R
import br.com.main.di.injectFeature
import br.com.main.modules.main.viewmodels.CreateCampViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCampActivity: AppCompatActivity() {

    private val viewModel: CreateCampViewModel by viewModel()

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_camp)

        injectFeature()
    }
}