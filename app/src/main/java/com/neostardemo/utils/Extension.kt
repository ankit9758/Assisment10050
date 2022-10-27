package com.neostardemo.utils

import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.neostardemo.R
import com.neostardemo.databinding.DialogUserInfoBinding
import com.neostardemo.models.User
import java.io.File
import java.util.regex.Pattern


fun isValidPassword(password: String): Boolean {
    val pattern = Pattern.compile(AppConstants.PASSWORD_PATTERN)
    val matcher = pattern.matcher(password)
    return matcher.matches()
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

interface DialogCallbackCameraGallery {
    fun onYes(from: String)
}

fun Context.showCameraGalleryDialog(callback: DialogCallbackCameraGallery) {
    val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setGravity(Gravity.BOTTOM)
    val lp: WindowManager.LayoutParams = dialog.window!!.attributes
    lp.dimAmount = 0.75f
    dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window
    dialog.window!!.attributes = lp
    dialog.setContentView(com.neostardemo.R.layout.dialog_camera_gallery)

    val tvCamera = dialog.findViewById(com.neostardemo.R.id.tv_Camera) as TextView
    val tvGallery = dialog.findViewById(com.neostardemo.R.id.tv_gallery) as TextView
    tvCamera.setOnClickListener {
        dialog.dismiss()
        callback.onYes(AppConstants.CAMERA)
    }
    tvGallery.setOnClickListener {
        dialog.dismiss()
        callback.onYes(AppConstants.GALLERY)
    }

    dialog.show()
}

fun Context.showUserInfoDialog(userInfo: User) {
    val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setGravity(Gravity.CENTER)
    val lp: WindowManager.LayoutParams = dialog.window!!.attributes
    lp.dimAmount = 0.75f
    dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.attributes = lp
    val binding: DialogUserInfoBinding = DataBindingUtil.inflate(
        LayoutInflater.from(this),
        R.layout.dialog_user_info, null, false
    )
    dialog.setContentView(binding.root)


    with(binding) {
        user = userInfo
        yearOfPassing=  "Passing year-${userInfo.passingYear}"
        grade=  "CGPA-${userInfo.grade}"
        experience=  "${userInfo.experience} years of experience"
        domain=  "${userInfo.domain} Department"
        ivUser.setImageURI(Uri.parse(userInfo.avatar))
        ivClose.setOnClickListener { dialog.dismiss() }

    }
    dialog.show()
}

fun Context.getRealPathFromUri(contentUri: Uri): String {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (contentUri.path!!.contains("/document/image:")) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(contentUri).split(":")[1])
        } else {
            databaseUri = contentUri
            selection = null
            selectionArgs = null
        }
        cursor = this.contentResolver.query(databaseUri, proj, selection, selectionArgs, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()

        cursor.getString(column_index)
    } catch (ex: Exception) {
        ex.printStackTrace()
        ""
    } finally {
        cursor?.close()
    }
}

