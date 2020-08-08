package com.teckudos.letshearit

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.teckudos.letshearit.soundtouch.SoundTouch
import java.io.File

class MainActivity1 : AppCompatActivity(), View.OnClickListener {
    var textViewConsole: TextView? = null
    var editSourceFile: EditText? = null
    var editOutputFile: EditText? = null
    var editTempo: EditText? = null
    var editPitch: EditText? = null
    var checkBoxPlay: CheckBox? = null
    var consoleText = StringBuilder()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        textViewConsole = findViewById<View>(R.id.textViewResult) as TextView
        editSourceFile = findViewById<View>(R.id.editTextSrcFileName) as EditText
        editOutputFile = findViewById<View>(R.id.editTextOutFileName) as EditText
        editTempo = findViewById<View>(R.id.editTextTempo) as EditText
        editPitch = findViewById<View>(R.id.editTextPitch) as EditText
        val buttonFileSrc =
            findViewById<View>(R.id.buttonSelectSrcFile) as Button
        val buttonFileOutput =
            findViewById<View>(R.id.buttonSelectOutFile) as Button
        val buttonProcess =
            findViewById<View>(R.id.buttonProcess) as Button
        buttonFileSrc.setOnClickListener(this)
        buttonFileOutput.setOnClickListener(this)
        buttonProcess.setOnClickListener(this)
        checkBoxPlay = findViewById<View>(R.id.checkBoxPlay) as CheckBox
        val sdDir = Environment.getExternalStorageDirectory()
            .toString() + "/Download/test.wav" //获取跟目录
        editSourceFile!!.setText(sdDir)
        editOutputFile!!.setText(sdDir)
        // Check soundtouch library presence & version
        checkLibVersion()
    }

    /// Function to append status text onto "console box" on the Activity
    fun appendToConsole(text: String?) {
        // run on UI thread to avoid conflicts
        runOnUiThread {
            consoleText.append(text)
            consoleText.append("\n")
            textViewConsole!!.text = consoleText
        }
    }

    /// print SoundTouch native library version onto console
    protected fun checkLibVersion() {
        val ver: String = SoundTouch.getVersionString()
        appendToConsole("SoundTouch native library version = $ver")
    }

    /// Button click handler
    override fun onClick(arg0: View) {
        when (arg0.id) {
            R.id.buttonSelectSrcFile, R.id.buttonSelectOutFile ->                 // one of the file select buttons clicked ... we've not just implemented them ;-)
                Toast.makeText(
                    this,
                    "File selector not implemented, sorry! Enter the file path manually ;-)",
                    Toast.LENGTH_LONG
                ).show()
            R.id.buttonProcess ->                 // button "process" pushed
                process()
        }
    }

    /// Play audio file
    protected fun playWavFile(fileName: String?) {
        val file2play = File(fileName)
        val i = Intent()
        i.action = Intent.ACTION_VIEW
        i.setDataAndType(Uri.fromFile(file2play), "audio/wav")
        startActivity(i)
    }

    /// Helper class that will execute the SoundTouch processing. As the processing may take
    /// some time, run it in background thread to avoid hanging of the UI.
    protected inner class ProcessTask :
        AsyncTask<ProcessTask.Parameters?, Int?, Long>() {
        /// Helper class to store the SoundTouch file processing parameters
        inner class Parameters {
            var inFileName: String? = null
            var outFileName: String? = null
            var tempo = 0f
            var pitch = 0f
        }

        /// Function that does the SoundTouch processing
        fun doSoundTouchProcessing(params: Parameters): Long {
            val st = SoundTouch()
            st.setTempo(params.tempo)
            st.setPitchSemiTones(params.pitch)
            Log.i("SoundTouch", "process file " + params.inFileName)
            val startTime = System.currentTimeMillis()
            val res = st.processFile(params.inFileName!!, params.outFileName!!)
            val endTime = System.currentTimeMillis()
            val duration = (endTime - startTime) * 0.001f
            Log.i("SoundTouch", "process file done, duration = $duration")
            appendToConsole("Processing done, duration $duration sec.")
            if (res != 0) {
                val err: String = SoundTouch.getErrorString()
                appendToConsole("Failure: $err")
                return -1L
            }

            // Play file if so is desirable
            if (checkBoxPlay!!.isChecked) {
                playWavFile(params.outFileName)
            }
            return 0L
        }

        /// Overloaded function that get called by the system to perform the background processing
        override fun doInBackground(vararg params: Parameters?): Long? {
            return params[0]?.let { doSoundTouchProcessing(it) }
        }
    }

    /// process a file with SoundTouch. Do the processing using a background processing
    /// task to avoid hanging of the UI
    protected fun process() {
        try {
            val task = ProcessTask()
            val params =
                task.Parameters()
            // parse processing parameters
            params.inFileName = editSourceFile!!.text.toString()
            params.outFileName = editOutputFile!!.text.toString()
            params.tempo = 0.01f * editTempo!!.text.toString().toFloat()
            params.pitch = editPitch!!.text.toString().toFloat()

            // update UI about status
            appendToConsole("Process audio file :" + params.inFileName + " => " + params.outFileName)
            appendToConsole("Tempo = " + params.tempo)
            appendToConsole("Pitch adjust = " + params.pitch)
            Toast.makeText(
                this,
                "Starting to process file " + params.inFileName + "...",
                Toast.LENGTH_SHORT
            ).show()

            // start SoundTouch processing in a background thread
            task.execute(params)
            //			task.doSoundTouchProcessing(params);	// this would run processing in main thread
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }
}