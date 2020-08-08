package com.teckudos.letshearit.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teckudos.letshearit.databinding.FragmentSoundPlayBinding
import com.teckudos.letshearit.viewmodels.MainViewModel
import java.lang.Exception


class SoundPlayFragment : Fragment() {

    private lateinit var binding: FragmentSoundPlayBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private lateinit var args: SoundPlayFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSoundPlayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        args = SoundPlayFragmentArgs.fromBundle(requireArguments())
        binding.outputPath = args.soundPath
        initObserver()
        return binding.root
    }

    private fun initObserver() {
        viewModel.audioPlaying.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                startPlaying()
                binding.animation.playAnimation()
            }
        })
    }

    private fun startPlaying() {
        val player = MediaPlayer()
        try {
            player.isLooping = false
            player.prepare()
            player.start()
        } catch (ex: Exception) {
            print("error in playing $ex")
        }
    }

}