package cn.krisez.kotlin.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import cn.krisez.kotlin.net.API
import cn.krisez.network.NetWorkUtils
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.bean.VersionBean
import cn.krisez.shareroute.llistener.DownloadListener
import cn.krisez.shareroute.utils.Const
import cn.krisez.shareroute.utils.SPUtil
import cn.krisez.shareroute.utils.Utils
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_about_question.view.*
import okhttp3.ResponseBody

class AboutAndQuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_about_question, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val s = "版本号：${Utils.getAppVersionName(context)}"
        view.version.text = s
        view.fankui.setOnClickListener {
            context?.let {
                val edit = EditText(it)
                AlertDialog.Builder(it).setTitle("填写您的建议/疑问").setView(edit)
                    .setPositiveButton(R.string.sure) { _, _ ->
                        if (edit.text.toString().isNotEmpty()) {
                            NetWorkUtils.INSTANCE().create(
                                NetWorkUtils.NetApi().api(API::class.java).fankui(
                                    edit.text.toString(),
                                    SPUtil.getUser().id
                                )
                            )
                                .handler(object : ResultHandler {
                                    override fun onSuccess(result: Result?) {
                                        Toast.makeText(context, result?.extra, Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    override fun onFailed(e: String?) {
                                        Toast.makeText(context, e, Toast.LENGTH_SHORT).show()
                                    }
                                })
                        }
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        }
        view.check_update.setOnClickListener {
            NetWorkUtils.INSTANCE().create(
                NetWorkUtils.NetApi().api(API::class.java).getLastVer(
                    Utils.getAppVersionName(context)
                )
            )
                .handler(object : ResultHandler {
                    override fun onSuccess(result: Result?) {
                        val bean = Gson().fromJson(result?.extra, VersionBean::class.java)
                        context?.let {
                            AlertDialog.Builder(it).setTitle("有新版本发布").setMessage("是否更新")
                                .setPositiveButton(R.string.sure) { _, _ ->
                                    Const.downLoadFile = true
                                    val d1 = NetWorkUtils.NetApi().api(API::class.java)
                                        .downloadFile(bean.url)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe { body ->
                                            Utils.writeFile2Disk(body, object : DownloadListener {
                                                override fun onProgress(progress: Int) {
                                                    Log.d("aaa","onProgress:$progress")
                                                    view.update_progress.progress = progress
                                                }

                                                override fun onFinish() {
                                                    Const.downLoadFile = false
                                                    val i = Intent(Intent.ACTION_VIEW)
                                                    val filePath = Environment.getExternalStorageDirectory().path + "/随行/download/lastVersion.apk"
                                                    i.setDataAndType(Uri.parse("file://$filePath"), "application/vnd.android.package-archive")
                                                    if ((Build.VERSION.SDK_INT >= 24)) {//判读版本是否在7.0以上
                                                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                    }
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    activity?.startActivity(i)
                                                }
                                            })
                                        }
                                }
                                .setNegativeButton(R.string.cancel, null)
                                .show()
                        }
                    }

                    override fun onFailed(e: String?) {
                        Toast.makeText(context, e, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

}