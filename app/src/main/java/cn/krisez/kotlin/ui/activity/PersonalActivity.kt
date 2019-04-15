package cn.krisez.kotlin.ui.activity

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.CheckPermissionsActivity
import cn.krisez.framework.base.Presenter
import cn.krisez.framework.utils.DensityUtil
import cn.krisez.kotlin.net.API
import cn.krisez.network.NetWorkUtils
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.bean.User
import cn.krisez.shareroute.utils.MD5Utils
import cn.krisez.shareroute.utils.SPUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_personal.*
import kotlinx.android.synthetic.main.dialog_revise_password.*
import kotlinx.android.synthetic.main.dialog_revise_password.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URISyntaxException

class PersonalActivity : BaseActivity() {
    override fun newView(): View = View.inflate(this, R.layout.activity_personal, null)

    override fun presenter(): Presenter? {
        return null
    }

    override fun init(bundle: Bundle?) {
        setUpToolbar()
        showBackIconAndClick()
        Glide.with(this).setDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_icon))
            .load(SPUtil.getUser().avatar).into(personal_avatar_at)
        personal_nickname_at.text=SPUtil.getUser().name
        personal_mobile_at.text=SPUtil.getUser().getMobile()
        personal_real_name_at.text=SPUtil.getUser().realName
        personal_avatar.setOnClickListener {
            AlertDialog.Builder(this).setItems(arrayOf("拍照上传", "本地相册")) { _, which ->
                if (which == 0) {
                    val intent = Intent("android.media.action.IMAGE_CAPTURE")
                    tempFile = File(
                        Environment.getExternalStorageDirectory().path + "/随行/imgs",
                        "temp.png"
                    )
                    val uri = Uri.fromFile(tempFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(intent, 100)//携带请求码
                } else {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    startActivityForResult(intent, 101)
                }
            }.show()
        }
        personal_nickname.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this).setTitle("来个好听的昵称吧~").setView(editText).setNegativeButton("取消",null)
                .setPositiveButton("确定") { _, _ ->
                    reviseUser(editText.text.toString(),"name")
                }
                .show()
        }
        personal_mobile.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this).setTitle("即将修改您的手机号~").setView(editText).setNegativeButton("取消",null)
                .setPositiveButton("确定") { _, _ ->
                    reviseUser(editText.text.toString(),"mobile")
                }
                .show()
        }
        personal_real_name.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this).setTitle("注意:此项仅为求助时使用(可不填)~").setView(editText).setNegativeButton("取消",null)
                .setPositiveButton("确定") { _, _ ->
                    val user = SPUtil.getUser()
                    user.realName = editText.text.toString()
                    reviseUser(editText.text.toString(),"realName")
                }
                .show()
        }
        personal_password.setOnClickListener {
            val v = View.inflate(this,R.layout.dialog_revise_password,null)
            AlertDialog.Builder(this).setTitle("修改您的密码~").setView(v).setNegativeButton("取消",null)
                .setPositiveButton("确定") { _, _ ->
                    if(v.dialog_new_pw.text.toString()==v.dialog_new_pw_again.text.toString()){
                        val pw = v.dialog_new_pw.text.toString()
                        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).updatePw(SPUtil.getUser().id,MD5Utils.encode(pw)))
                            .handler(object:ResultHandler{
                                override fun onSuccess(result: Result?) {
                                    toast(result?.extra)
                                }

                                override fun onFailed(s: String?) {
                                    error(s)
                                }
                            })
                    }else{
                        error("两次密码不一致")
                    }
                }
                .show()}
    }

    private fun reviseUser(value: String?,who:String){
        val user = SPUtil.getUser()
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).updateUser(user.id,value,who))
            .handler(object :ResultHandler{
                override fun onSuccess(result: Result?) {
                    when(who){
                        "name"->{
                            user.name = value
                            personal_nickname_at.text=user?.name
                        }
                        "mobile"-> {
                            user.mobile = value
                            personal_mobile_at.text = user?.getMobile()
                        }
                        "realName"->{
                            user.realName = value
                            personal_real_name_at.text=user?.realName
                        }
                    }
                    SPUtil.saveUser(user)
                }

                override fun onFailed(s: String?) {
                    error(s)
                }
            })
    }

    override fun onRefresh() {

    }

    private var tempFile: File? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            when (requestCode) {
                100 -> {
                    upLoad2Server(tempFile?.path)
//                crop(Uri.fromFile(tempFile))
                }
                101 -> {
                    if (resultCode == CheckPermissionsActivity.RESULT_OK) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            handlePathOnKitKat(data)
                        } else {
                            handlePathBeforeKitKat(data)
                        }
                    }
                }
                103 -> {
                    if (data != null) {
                        val bitmap = data.getParcelableExtra("data") as Bitmap
                        //将bitmap转换为Uri
                        val uri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bitmap, null, null))
                        //对非正确的Uri处理，这类Uri存在手机的external.db中，可以查询_data字段查出对应文件的uri
                        //在这可以拿到裁剪后的图片Uri,然后进行你想要的操作
                        upLoad2Server(uri.path)
                    }
                }
            }
        }
        try {
            tempFile?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePathBeforeKitKat(data: Intent?) {
        upLoad2Server(data?.data?.path)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun handlePathOnKitKat(data: Intent?) {
        var path: String? = null
        val uri = data?.data
        when {
            DocumentsContract.isDocumentUri(this, uri) -> {
                val docId = DocumentsContract.getDocumentId(uri)
                when {
                    "com.android.providers.media.documents" == uri?.authority -> {
                        val id = docId.split(":")[1]
                        val selection = MediaStore.Images.Media._ID + "=" + id
                        path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                    }
                    "com.android.providers.downloads.documents" == uri?.authority -> {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            docId.toLong()
                        )
                        path = getImagePath(contentUri, null)
                    }
                    "com.android.externalstorage.documents" == uri?.authority -> path =
                        Environment.getExternalStorageDirectory().path + "/" + uri.path.split(":")[1]
                }
            }
            "content".equals(uri?.scheme, true) -> path = getImagePath(uri, null)
            "file".equals(uri?.scheme, true) -> path = uri?.path
        }
        upLoad2Server(path)
    }

    private fun getImagePath(uri: Uri?, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun upLoad2Server(path: String?) {
        var file: File? = null
        try {
            file = File(DensityUtil.compressImage(path, Environment.getExternalStorageDirectory().path + "/随行/imgs/" + System.currentTimeMillis() + ".png", 10))
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("mFile", file?.name, requestBody)
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).uploadFile(SPUtil.getUser().id,body))
            .handler(object : ResultHandler {
                override fun onSuccess(result: Result?) {
                    val user = SPUtil.getUser()
                    user.avatar = result?.extra
                    SPUtil.saveUser(user)
                    Glide.with(this@PersonalActivity)
                        .setDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_icon))
                        .load(user.avatar).into(personal_avatar_at)
                }

                override fun onFailed(s: String?) {
                    Toast.makeText(this@PersonalActivity, s, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun crop(uri: Uri?) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250)
        intent.putExtra("outputY", 250)
        intent.putExtra("outputFormat", "JPEG")// 图片格式
        intent.putExtra("noFaceDetection", true)// 取消人脸识别
        intent.putExtra("return-data", true)
        startActivityForResult(intent, 103) // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
    }
}
