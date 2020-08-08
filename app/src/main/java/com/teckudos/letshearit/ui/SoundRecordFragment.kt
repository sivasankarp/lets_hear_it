package com.teckudos.letshearit.ui

import android.content.Context
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teckudos.letshearit.databinding.FragmentSoundRecordBinding
import com.teckudos.letshearit.viewmodels.MainViewModel
import java.io.File
import java.util.*

class SoundRecordFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private lateinit var uiCallbacksListener: UICallbacksListener
    private lateinit var binding: FragmentSoundRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSoundRecordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startRecording.setOnClickListener {
            if (uiCallbacksListener.isPermissionsGranted()) {
                val file = context?.getExternalFilesDir(null)
                val inputFileName = "input_${Calendar.getInstance().toEpochSec()}.wav"
                val inputFile = File(file, inputFileName)
                viewModel.inputFilePath = inputFile.path
                val outputFileName = inputFileName.replace("input_", "output_")
                val outputFile = File(file, outputFileName)
                viewModel.outputFilePath = outputFile.path
                viewModel.onStart()
            }
        }
    }

    private fun Calendar.toEpochSec(): Long = this.timeInMillis / 1000

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCallbacksListener = context as UICallbacksListener
        } catch (e: ClassCastException) {
            Log.e("Error", "$context must implement UICommunicationListener")
        }
    }
}