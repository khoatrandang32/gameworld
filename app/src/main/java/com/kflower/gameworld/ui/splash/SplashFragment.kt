package com.kflower.gameworld.ui.splash

import android.os.Bundle
import android.os.Handler
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.ui.main.MainFragment

import androidx.databinding.ViewDataBinding
import com.kflower.gameworld.databinding.SplashFragmentBinding

import com.kflower.gameworld.R

import android.util.Log

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kflower.gameworld.dialog.LoadingDialog
import android.os.Environment
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.appFragmentManager
import com.kflower.gameworld.MyApplication.Companion.downloadTable
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.common.renderRandomId
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.model.DownloadAudio
import java.io.File


class SplashFragment : BaseChildFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var binding: SplashFragmentBinding;
    lateinit var viewModel: SplashViewModel;
    lateinit var googleSignInClient: GoogleSignInClient;
    lateinit var auth: FirebaseAuth;
    lateinit var dialog: LoadingDialog;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        dialog = LoadingDialog(requireContext());
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        viewModel = SplashViewModel();
        auth = FirebaseAuth.getInstance();

        binding = SplashFragmentBinding.inflate(layoutInflater)

        binding.viewModel = viewModel;

        appFragmentManager= parentFragmentManager

        checkFilesInStorage();

        checkDownloadingData();
        checkDownloadedData();

        Handler().postDelayed({
//            finish()
            navigateTo(MainFragment.newInstance())

        }, 3000)

    }


    private fun checkDownloadedData() {
//        var listDownloaded= downloadTable.findDownloadsByState(DownloadState.COMPLETED)
//        if(listDownloaded.size>0){
//            MyApplication.listDownloaded.postValue(listDownloaded);
//        }
    }

    private fun checkDownloadingData() {
//        var listDownloading= downloadTable.findDownloadsByState(DownloadState.DOWNLOADING)
//        if(listDownloading.size>0){
//            MyApplication.listDownloading.postValue(listDownloading);
//        }
    }




    private fun checkFilesInStorage() {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/TopTopFiles"
        val directory = File(path)
        try {
            val files: Array<File> = directory.listFiles()
            for (i in files.indices) {
                try {
                    var fileName = files[i].name
                    var wrapFileName = fileName.substring(0, fileName.length - 4);
                    var listStr = wrapFileName.split("_ID_")
                    var audioId = listStr[0];
                    var audioEp = listStr[1].toInt() - 1;
                    var downloadItem = DownloadAudio(
                        audioId = audioId,
                        ep = audioEp,
                        state = DownloadState.COMPLETED,
                        progress = 100,
                        id = renderRandomId(),
                        uri = "$path/$fileName"
                    )
                    if (downloadTable.findDownloadByAudioId(audioId, audioEp) == null) {
                        downloadTable.addNewDownload(downloadItem)
                    }

                } catch (e: Exception) {
                    Log.d(TAG, "checkFilesInStorage error: " + e.message)
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }


}