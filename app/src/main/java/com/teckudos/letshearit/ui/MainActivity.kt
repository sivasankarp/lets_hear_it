package com.teckudos.letshearit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.teckudos.letshearit.R
import com.teckudos.letshearit.databinding.ActivityMainBinding
import com.teckudos.letshearit.utils.Counter
import com.teckudos.letshearit.viewmodels.MainViewModel
import com.teckudos.letshearit.viewmodels.MainViewModelProviderFactory

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val counter = Counter(this.lifecycle)
        val viewModelFactory = MainViewModelProviderFactory(counter)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        navController = this.findNavController(R.id.nav_host_fragment)
    }

}