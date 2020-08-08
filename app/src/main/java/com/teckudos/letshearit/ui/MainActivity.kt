package com.teckudos.letshearit.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.teckudos.letshearit.R
import com.teckudos.letshearit.databinding.ActivityMainBinding
import com.teckudos.letshearit.utils.Counter
import com.teckudos.letshearit.viewmodels.SoundRecordViewModel
import com.teckudos.letshearit.viewmodels.RecordViewModelProviderFactory

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = this.findNavController(R.id.nav_host_fragment)

        initListeners()
    }

    private fun initListeners() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == controller.graph.startDestination) {
                this@MainActivity.title = getString(R.string.sound_record)
            } else {
                this@MainActivity.title = getString(R.string.sound_play)
            }
        }
    }

}