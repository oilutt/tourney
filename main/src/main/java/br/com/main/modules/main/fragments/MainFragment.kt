package br.com.main.modules.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import br.com.commons.SampleNavigation
import br.com.commons.SampleNavigation.CREATE_CAMP_ACTIVITY
import br.com.main.R
import br.com.utils.animations.BottomNavigationViewBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutParams = bottom_navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior =
            BottomNavigationViewBehavior(object : BottomNavigationViewBehavior.Callback {
                override fun onSlideUp() {

                }

                override fun onSlideDown() {

                }
            })

        fab.setOnClickListener {
            startActivity(
                SampleNavigation.navigateActivity(
                    CREATE_CAMP_ACTIVITY
                )
            )
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment = when (item.itemId) {
            R.id.search -> {
                SearchFragment()
            }
            R.id.settings -> {
                SettingsFragment()
            }
            else -> {
                MeusCampeonatosFragment()
            }
        }

        swapFragment(fragment)
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {

    }

    private fun swapFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
    }
}