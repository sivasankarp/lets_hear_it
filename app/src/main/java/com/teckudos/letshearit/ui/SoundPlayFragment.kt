package com.teckudos.letshearit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teckudos.letshearit.databinding.FragmentSoundPlayBinding
import com.teckudos.letshearit.viewmodels.MainViewModel

class SoundPlayFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSoundPlayBinding.inflate(inflater, container, false)

        return binding.root
    }

}