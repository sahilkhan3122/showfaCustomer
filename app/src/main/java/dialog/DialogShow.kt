package dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import extension.dialogShow
import extension.hideSoftKeyboard
import extension.showSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import laungage.MyDataStore
import model.AddAddressFavModel
import model.CancellationModel
import ui.homeActivity.HomeActivity
import ui.otpVerification.OtpVerificationActivity
import ui.paymentActivity.PaymentWalletActivity
import ui.tripHistory.CancellationAdapter
import ui.welcome.WelcomeActivity
import utils.API_CONSTANTS.AMOUNT
import utils.API_CONSTANTS.MOBILENUMBER
import utils.API_CONSTANTS.liveData

class DialogShow(var activity: Activity) {
    var dialog: AlertDialog? = null
    private val inflater = activity.layoutInflater
    private val bottomSheet = BottomSheetDialog(activity, R.style.BottomSheetDialog)
    val infleter = activity.layoutInflater
    val view = infleter.inflate(R.layout.pickup_fav_bottomsheet, null)

    fun networkDialog(
        txtTitle: Int,
        buttonClose: Int,
        txtmessage: String,
        txtConnection: Int,
        type: Int? = null,
        cancle: (() -> Unit)? = null,
        okbutton: (() -> Unit)? = null
    ) {

        val dialogView = inflater.inflate(R.layout.item_dialog_error, null)
        val dialogBuilder = activity.dialogShow(dialogView = dialogView.rootView)

        val txtTittle = dialogView?.findViewById<TextView>(R.id.internetConnection)
        val imgClose = dialogView?.findViewById<ImageView>(R.id.imgClose)
        val txtNetworkConnection = dialogView?.findViewById<TextView>(R.id.pleaseCheckNetwork)
        val connection = dialogView?.findViewById<TextView>(R.id.txtConnection)
        val okButton = dialogView?.findViewById<Button>(R.id.btnOk)

        txtTittle?.visibility = txtTitle
        imgClose?.visibility = buttonClose
        txtNetworkConnection?.text = txtmessage
        connection?.visibility = txtConnection


        imgClose?.setOnClickListener {
            dialog?.dismiss()
        }
        okButton?.setOnClickListener {
            cancle?.invoke()
            dialog?.dismiss()
        }
        //support
        if (type == 0) {
            txtTittle?.text = activity.getString(R.string.success)
        }
        //login
        else if (type == 1) {
            okButton?.setOnClickListener {
                val intent = (Intent(activity, OtpVerificationActivity::class.java))
                intent.putExtra("LOGININDEX", 1)
                activity.startActivity(intent)
                activity.finish()

            }

        } else if (type == 2) {
            txtTittle?.text = activity.getString(R.string.success)
            okButton?.setOnClickListener {
                val intent = (Intent(activity, HomeActivity::class.java))
                activity.startActivity(intent)
                activity.finish()
            }
        }//home add fav
        else if (type == 3) {
            dialog?.dismiss()

        }
        dialog = dialogBuilder.create()
        dialog?.setCancelable(false)
        dialog!!.create()
        dialog!!.show()
    }

    @SuppressLint("MissingInflatedId")
    fun sessionExpiredDialogShow(
        context: Context,
        message: String,
        btnOkTextSet: String,
        btnCancelTextSet: String,
        type: Int,
        myDataStore: MyDataStore
    ) {

        val dialogView: View = inflater.inflate(R.layout.session_expire_dialog, null)
        val dialogBuilder = activity.dialogShow(dialogView = dialogView.rootView)
        dialogBuilder.setView(dialogView)

        val session = dialogView.findViewById<TextView>(R.id.txtSessionExpired)
        val btnOk =
            dialogView.findViewById<Button>(R.id.btnOk)
        val btnCancel =
            dialogView.findViewById<Button>(R.id.btnCancel)

        session.text = message
        btnOk.text = btnOkTextSet
        btnCancel.text = btnCancelTextSet

        if (type == 1) {
            btnOk.setOnClickListener {
                context.startActivity(Intent(context, WelcomeActivity::class.java))
                activity.finish()
            }
            btnCancel.setOnClickListener {
                context.startActivity(Intent(context, WelcomeActivity::class.java))
                activity.finish()
            }
        }

        if (type == 2) {
            btnOk.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    myDataStore.logoutId()
                }
                context.startActivity(Intent(context, WelcomeActivity::class.java))
                activity.finish()
            }
            btnCancel.setOnClickListener {
                context.startActivity(Intent(context, HomeActivity::class.java))
                activity.finish()
            }
        }
        var dialog = dialogBuilder.create()
        dialog?.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    fun bottomSheet(
        txtAddress: String = "",
        ischecked: Boolean = false,
        activity: AppCompatActivity,
        btnAddPlaceClick: (() -> Unit)? = null,
        btnClick: (() -> Unit)? = null,
        title: String,
        amount: String? = "",
        mobilenumber: String? = "",
        txtAddFavClick: (() -> Unit)? = null,
        list: ArrayList<CancellationModel> = arrayListOf(),
        addAddressList: ArrayList<AddAddressFavModel.Data>? = null

    ): BottomSheetDialog {
        var cancellationAdapter: CancellationAdapter? = null
        val language = arrayOf(
            activity.getString(R.string.selected_user),
            activity.getString(R.string.coustomer),
            activity.getString(R.string.driver)
        )


        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        val txtSearchPlace = view.findViewById<TextView>(R.id.txtSearchPlace)
        val txtAddressFav = view.findViewById<EditText>(R.id.txtAddressFav)
        val btnAddPlaces = view.findViewById<Button>(R.id.btnAddPlace)
        val imgLocation = view.findViewById<ImageView>(R.id.imgLocation)
        val radioGroup = view.findViewById<RadioGroup>(R.id.idRadioButton)
        val home = view.findViewById<RadioButton>(R.id.txtHome)
        val office = view.findViewById<RadioButton>(R.id.txtOffice)
        val other = view.findViewById<RadioButton>(R.id.txtOther)
        val spinnerCustomerAndDriver = view.findViewById<Spinner>(R.id.spinnerCustomerAndDriver)


        val imgFlag = view.findViewById<ImageView>(R.id.imgFlag)
        val flagCode = view.findViewById<TextView>(R.id.flagCode)
        val codeViewLine = view.findViewById<View>(R.id.editNumberViewLine)
        val viewLineAddress = view.findViewById<View>(R.id.viewLineAddress)
        val edtNumber = view.findViewById<EditText>(R.id.edtNumber)
        val flagView = view.findViewById<View>(R.id.flagView)
        val imageDropDown = view.findViewById<View>(R.id.imageDropDown)
        val sendingMoney = view.findViewById<TextView>(R.id.sendingMoney)
        val sendNumber = view.findViewById<TextView>(R.id.sendNumber)
        val customerAndDriverNumberSame =
            view.findViewById<TextView>(R.id.customerAndDriverNumberSame)
        val spinnerCl = view.findViewById<LinearLayout>(R.id.spinnerCl)

        if (home.isChecked) {
            addAddressList?.add(AddAddressFavModel.Data(favouriteType = "home"))
        } else if (office.isChecked) {
            addAddressList?.add(AddAddressFavModel.Data(favouriteType = "office"))
        } else {
            addAddressList?.add(AddAddressFavModel.Data(favouriteType = "other"))
        }

        if (title == "AddNewPlaces") {
            txtAddressFav.isEnabled = true
            txtAddressFav.setOnClickListener {
                hideSoftKeyboard(activity)
                txtAddFavClick?.invoke()
            }
            liveData.observe(activity) {
                txtAddressFav.setText(it)
                Log.d("TAG", "data===>$it")
            }
            btnAddPlaces.setOnClickListener {
                btnAddPlaceClick?.invoke()
            }

            bottomSheet.setCancelable(true)
            bottomSheet.setContentView(view)
            bottomSheet.show()
        }

        //wallet add money
        if (title == "imgAddMoney") {

            txtSearchPlace.text = txtAddress
            txtAddressFav.hint = activity.resources.getString(R.string.enterAmount)
            txtAddressFav.setHintTextColor(activity.resources.getColor(R.color.darkGray))
            btnAddPlaces.text = txtAddress
            radioGroup.visibility = View.GONE
            imgLocation.visibility = View.GONE
            bottomSheet.setContentView(view)

            btnAddPlaces.setOnClickListener {

                if (txtAddressFav.text.isEmpty()) {
                    view.rootView.showSnackBar(activity.getString(R.string.please_enter_amount))
                } else if (txtAddressFav.text.isNotEmpty()) {
                    val intent = Intent(activity, PaymentWalletActivity::class.java)
                    intent.putExtra("txtAmount", txtAddressFav.text.toString())
                    activity.startActivity(intent)
                    activity.finish()
                } else {
                    bottomSheet.dismiss()
                    btnAddPlaceClick?.invoke()
                }

            }
            bottomSheet.show()
        }
        if (title == "cancelTripApi") {
            val viewDialogBottomSheet =
                infleter.inflate(R.layout.dialog_cancelletion_bottomsheet, null)
            val recyclerView =
                viewDialogBottomSheet.findViewById<RecyclerView>(R.id.cancellationItemRecyclerView)
            val btnGoBack = viewDialogBottomSheet.findViewById<Button>(R.id.btnGoBack)
            val checkbox = viewDialogBottomSheet.findViewById<Button>(R.id.checkbox)
            val btnCancelRide = viewDialogBottomSheet.findViewById<Button>(R.id.btnCancelRide)
            cancellationAdapter = CancellationAdapter(activity, list) { position ->

                list[position].isCheck = !list[position].isCheck
            }
            btnGoBack.setOnClickListener {
                bottomSheet.dismiss()
            }
            btnCancelRide.setOnClickListener {
                btnAddPlaceClick?.invoke()
                bottomSheet.dismiss()

            }
            recyclerView.adapter = cancellationAdapter
            bottomSheet.setContentView(viewDialogBottomSheet)
            bottomSheet.setCancelable(false)
        }

        //wallet send money
        if (title == "imgSentMoney") {

            txtSearchPlace.text = txtAddress
            imgLocation.visibility = View.GONE
            txtAddressFav.hint = activity.resources.getString(R.string.sent_money)
            txtAddressFav.isFocusable = true;
            txtAddressFav.isFocusableInTouchMode = true;
            txtAddressFav.isClickable = true;
            btnAddPlaces.text = txtAddress
            MOBILENUMBER = edtNumber.text.toString()
            AMOUNT = txtAddressFav.text.toString()
            radioGroup.visibility = View.INVISIBLE
            imgFlag.visibility = View.VISIBLE
            flagCode.visibility = View.VISIBLE
            codeViewLine.visibility = View.VISIBLE
            edtNumber.visibility = View.VISIBLE
            flagView.visibility = View.VISIBLE

            imageDropDown.setOnClickListener {
                if (spinnerCustomerAndDriver != null) {
                    val adapter =
                        ArrayAdapter(activity, android.R.layout.simple_spinner_item, language)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCustomerAndDriver.adapter = adapter
                }
            }
            btnAddPlaces.setOnClickListener {
                if (txtAddressFav.text.isEmpty()) {
                    view.rootView.showSnackBar(activity.getString(R.string.please_enter_amount))
                } else if (edtNumber.text.isEmpty()) {
                    view.rootView.showSnackBar(activity.getString(R.string.please_enter_number))
                } else {
                    MOBILENUMBER = edtNumber.text.toString()
                    AMOUNT = txtAddressFav.text.toString()
                    btnAddPlaceClick?.invoke()
                    bottomSheet.dismiss()
                }
            }
            bottomSheet.setContentView(view)
        }

        //home pickUp address fav
        if (title == "imgPickUpFavourite") {
            txtSearchPlace.text = activity.getString(R.string.search_place)
            btnAddPlaces.text = activity.getString(R.string.search_place)
            txtAddressFav.setText(txtAddress)
            bottomSheet.setContentView(view)

            btnAddPlaces.setOnClickListener {
                btnAddPlaceClick?.invoke()
            }


        }
        //home dropOff address fav
        if (title == "imgDroupOffFavourite") {
            txtSearchPlace.text = activity.getString(R.string.search_place)
            btnAddPlaces.text = activity.getString(R.string.search_place)
            txtAddressFav.setText(txtAddress)
            bottomSheet.setContentView(view)

            btnAddPlaces.setOnClickListener {
                btnAddPlaceClick?.invoke()
            }

        }
        //confirm patyment coustomer and driver
        if (title == "checkMobileNumber") {
            txtSearchPlace.setText(txtAddress)
            txtAddressFav.visibility = View.INVISIBLE
            imgLocation.visibility = View.INVISIBLE
            viewLineAddress.visibility = View.INVISIBLE
            radioGroup.visibility = View.INVISIBLE

            sendingMoney.visibility = View.VISIBLE
            sendNumber.visibility = View.VISIBLE
            customerAndDriverNumberSame.visibility = View.VISIBLE
            spinnerCl.visibility = View.VISIBLE
            btnAddPlaces.text = activity.getText(R.string.sendMoney)

            bottomSheet.setContentView(view)
        }

        //confirm patyment
        if (title == "ConfirmPAyment") {
            txtAddressFav.visibility = View.GONE
            imgLocation.visibility = View.GONE
            viewLineAddress.visibility = View.GONE
            radioGroup.visibility = View.GONE

            txtSearchPlace.setText(txtAddress)
            btnAddPlaces.text = activity.getString(R.string.sent_money)
            sendingMoney.visibility = View.VISIBLE
            sendNumber.visibility = View.VISIBLE

            sendingMoney.text = "Sending Key $amount"
            sendNumber.text = "to +$mobilenumber"

            bottomSheet.setContentView(view)

        }
        imgClose.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.setCancelable(true)
        bottomSheet.show()
        return bottomSheet
    }


}










