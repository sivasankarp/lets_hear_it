package com.teckudos.letshearit.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teckudos.letshearit.R
import com.teckudos.letshearit.databinding.FragmentSoundRecordBinding
import com.teckudos.letshearit.utils.Counter
import com.teckudos.letshearit.viewmodels.RecordViewModelProviderFactory
import com.teckudos.letshearit.viewmodels.SoundRecordViewModel
import java.io.File
import java.util.*

class SoundRecordFragment : Fragment() {

    private lateinit var binding: FragmentSoundRecordBinding
    private lateinit var viewModel: SoundRecordViewModel

    private lateinit var uiCallbacksListener: UICallbacksListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSoundRecordBinding.inflate(inflater, container, false)

        val counter = Counter(this.lifecycle)
        val viewModelFactory = RecordViewModelProviderFactory(counter)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SoundRecordViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initObserver()
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
                val outputFileName =
                    inputFileName.replace(getString(R.string.input), getString(R.string.output))
                val outputFile = File(file, outputFileName)
                viewModel.outputFilePath = outputFile.path
                viewModel.onStart()
            }
        }
    }

    private fun initObserver() {
        viewModel.navigateToPlay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                this.findNavController().navigate(
                    SoundRecordFragmentDirections.actionSoundRecordFragmentToSoundPlayFragment(
                        viewModel.outputFilePath
                    )
                )
                viewModel.navigatedToPlay()
            }
        })
    }

    private fun Calendar.toEpochSec(): Long = this.timeInMillis / 1000

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCallbacksListener = context as UICallbacksListener
        } catch (e: ClassCastException) {
            Log.e("Error", "$context must implement UICallbacksListener")
        }
    }
}