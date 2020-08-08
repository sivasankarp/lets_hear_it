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
import com.teckudos.letshearit.viewmodels.SoundPlayViewModel


class SoundPlayFragment : Fragment() {

    private lateinit var binding: FragmentSoundPlayBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(SoundPlayViewModel::class.java)
    }

    private lateinit var args: SoundPlayFragmentArgs

    private lateinit var player: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSoundPlayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        args = SoundPlayFragmentArgs.fromBundle(requireArguments())

        init()
        initObserver()

        return binding.root
    }

    private fun init() {
        player = MediaPlayer()
    }

    private fun initObserver() {
        viewModel.audioPlaying.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    startPlaying()
                    binding.animation.playAnimation()
                } else {
                    player.stop()
                    player.reset()
                }
            }
        })
    }

    private fun startPlaying() {
        try {
            player.setDataSource(args.soundPath)
            player.isLooping = false
            player.prepare()
            player.setOnCompletionListener {
                binding.animation.cancelAnimation()
                viewModel.audioPlaying.value = false
            }
            player.start()
        } catch (ex: Exception) {
            binding.animation.cancelAnimation()
            viewModel.audioPlaying.value = false
            player.stop()
            player.reset()
            print("error in playing $ex")
        }
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        print("DEBUG fragment destroyed")
    }

}