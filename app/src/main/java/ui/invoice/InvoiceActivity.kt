package ui.invoice

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityInvoice2Binding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import extension.showSnackBar
import ui.tripHistory.TripHistoryActivity
import utils.FileDownloader
import java.io.File


@AndroidEntryPoint
class InvoiceActivity() : BaseActivity<ActivityInvoice2Binding, InvoicesViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_invoice2
    override val bindingVariable: Int
        get() = BR.viewModel
    lateinit var launcher: ActivityResultLauncher<String>
    private var invoiceFileName = ""
    lateinit var dialog: Dialog


    companion object {
        var TAG = TripHistoryActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.invoiceToolbar.txtTittle.text = getString(R.string.invoice)
        dataGetTripDetail()
        dialog = Dialog(this)
        binding.invoiceToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.invoicelayout.setOnClickListener {
            launcher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    mViewModel.downloadInvoiceApiCall()

                } else {
                    binding.root.showSnackBar(getString(R.string.please_storege_permisstion))
                }

            }
    }

    private fun dataGetTripDetail() {
        if (intent != null) {
            val pickupAddress = intent.extras?.getString("pickupAdress")
            val dropOffAddress = intent.extras?.getString("dropOffAdress")
            Log.d(TAG, "pickupAddress--->$pickupAddress")
            Log.d(TAG, "dropOffAddress--->$dropOffAddress")
            binding.txtPickUpAddress.text = pickupAddress
            binding.txtDropOffAddress.text = dropOffAddress
        }

    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.invoiceLiveData.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    val link = it.data?.data
                    val bits = link?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }
                        ?.toTypedArray()
                    val lastOne = bits?.get(bits.size - 1)
                    invoiceFileName = "showfa_$lastOne"
                    DownloadFile().execute(it.data!!.data)
                    Log.d(TAG, "URLDATA--->${it.data.data}")
                }
                Status.ERROR -> {
                    Log.d(TAG, "Error----->")
                }

                Status.LOADING -> {
                    Log.d(TAG, "Loading----->")
                }
            }
        }


    }

    @SuppressLint("StaticFieldLeak")
    inner class DownloadFile : AsyncTask<String, Void, Void>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String): Void? {

            val file = params[0]
            val fileName = invoiceFileName
            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val pdfFile = File(folder, fileName)
            try {
                pdfFile.createNewFile()

            } catch (e: Exception) {
                Log.d(TAG, "EXAPTION===>$e")

            }
            FileDownloader.downloadFile(file, pdfFile)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            runOnUiThread {
                openPDFFile()
                Toast.makeText(
                    this@InvoiceActivity,
                    getString(R.string.download_invoice),
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        private fun openPDFFile() {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                invoiceFileName
            )
            val photoURI = FileProvider.getUriForFile(
                this@InvoiceActivity,
                "com.example.showfa_customer_android.provider",
                file
            )
            if (file.exists()) //Checking for the file is exist or not
            {
                //Uri path = Uri.fromFile(pdfFile);
                val objIntent = Intent(Intent.ACTION_VIEW)
                objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                objIntent.setDataAndType(photoURI, "application/pdf")
                startActivity(objIntent) //Staring the pdf viewer
            }

        }


    }
}