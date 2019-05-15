package cn.krisez.kotlin.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.krisez.framework.base.CheckPermissionsActivity
import cn.krisez.imchat.ChatModuleManager
import cn.krisez.imchat.client.ImConst
import cn.krisez.imchat.client.WebSocketTransfer
import cn.krisez.imchat.manager.MessageManager
import cn.krisez.imchat.receiver.MessageReceiver
import cn.krisez.imchat.services.IMMsgService
import cn.krisez.kotlin.net.API
import cn.krisez.kotlin.ui.views.IMapView
import cn.krisez.network.NetWorkUtils
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.bean.UrgentBean
import cn.krisez.shareroute.bean.VersionBean
import cn.krisez.shareroute.event.MyLocationEvent
import cn.krisez.shareroute.llistener.DownloadListener
import cn.krisez.shareroute.maps.MapController
import cn.krisez.shareroute.maps.MarkerInfoWindow
import cn.krisez.shareroute.ui.LoadFragment
import cn.krisez.shareroute.utils.Const
import cn.krisez.shareroute.utils.SPUtil
import cn.krisez.shareroute.utils.Utils
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyTrafficStyle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : CheckPermissionsActivity(), IMapView, MessageReceiver {

    private var TRACE_HISTORY_CODE = 701
    private var isExpand = true

    private var controller: MapController? = null
    private var mMarkerInfoWindow: MarkerInfoWindow? = null

    private var mMapView: MapView? = null
    private var mAddress: TextView? = null

    private var mAMap: AMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        ImConst.id = SPUtil.getUser().id
        startService(Intent(this, IMMsgService::class.java).putExtra("user", SPUtil.getUser().toString()).putExtra("cls", MainActivity::class.java))
        initView(savedInstanceState)
        initMap(savedInstanceState)
    }

    private fun initMap(savedInstanceState: Bundle?) {
        controller = MapController(this)
        controller?.map(mMapView)?.view(this)?.defaultAmap()?.create(savedInstanceState)

        mAMap = mMapView?.map
        val myTrafficStyle = MyTrafficStyle()
        myTrafficStyle.seriousCongestedColor = Color.parseColor("#790000")
        myTrafficStyle.congestedColor = Color.parseColor("#ff0000")
        myTrafficStyle.slowColor = Color.parseColor("#a9c433")
        myTrafficStyle.smoothColor = Color.parseColor("#5c71fc71")
        mAMap?.myTrafficStyle = myTrafficStyle
        mAMap?.isTrafficEnabled = true

        mMarkerInfoWindow = MarkerInfoWindow(this)
        mAMap?.setInfoWindowAdapter(mMarkerInfoWindow)

        mAMap?.setOnMarkerClickListener {
            Toast.makeText(this@MainActivity, it.title, Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun initView(savedInstanceState: Bundle?) {
        mMapView = findViewById(R.id.map_view)
        mAddress = findViewById(R.id.main_user_address)

        main_user_id.text = SPUtil.getUser().id
        main_user_avatar.setOnClickListener {
            startActivity(Intent(this, PersonalActivity::class.java))
        }
        main_user_setup.setOnClickListener {
            startActivity(Intent(this, LoadFragment::class.java).putExtra("cls", "set"))
        }
        main_user_urgent.setOnClickListener {
            if(!Utils.isConnect(this)){
                Toast.makeText(this@MainActivity, "网络不可用...", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!Const.isUrgent) {
                MessageManager.send(Gson().toJson(WebSocketTransfer(77, Gson().toJson(UrgentBean(SPUtil.getUser().id, SPUtil.getEmergency(), SPUtil.getUser().realName)))))
                MessageManager.addReceiver(77, this)
                Const.isUrgent = true
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    main_user_urgent.drawable.setTint(resources.getColor(R.color.vector_reset))
                }
                Toast.makeText(this@MainActivity, "取消求助...", Toast.LENGTH_LONG).show()
                MessageManager.removeReceiver(77)
                MessageManager.send(Gson().toJson(WebSocketTransfer(-77, SPUtil.getUser().id)))
                Const.isUrgent = true
            }
        }
        main_user_alert.setOnClickListener {
            if(!Const.isHelp){
                val editText = EditText(this)
                AlertDialog.Builder(this).setTitle("输入您收到的求助码").setView(editText).setPositiveButton(R.string.sure) { _, _ ->
                    if (editText.text.toString().isNotEmpty()) {
                        controller?.startNewHelp(0,editText.text.toString())
                        Const.isHelp = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            main_user_alert.drawable.setTint(resources.getColor(R.color.colorAccent))
                        }
                    }
                }.setNegativeButton(R.string.cancel, null).show()

            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    main_user_alert.drawable.setTint(resources.getColor(R.color.vector_reset))
                }
                Const.isHelp = false
                Toast.makeText(this,"取消帮助",Toast.LENGTH_SHORT).show()
            }

        }
        main_user_message.setOnClickListener {
            ChatModuleManager.open(this, SPUtil.getUser().toString())
            main_msg_tips_dot.visibility = View.INVISIBLE
        }
        main_user_history.setOnClickListener {
            startActivityForResult(
                Intent(this, TraceHistoryActivity::class.java), TRACE_HISTORY_CODE
            )
        }
        main_user_mail.setOnClickListener {
            startActivity(Intent(this,SysNotifyActivity::class.java))
        }
        main_user_upload_location.setOnClickListener {
            if (Const.uploadLocation) {
                Const.uploadLocation = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val view = it as ImageView
                    view.drawable.setTint(resources.getColor(R.color.vector_reset))
                } else {
                    Toast.makeText(this, "停止上传定位数据", Toast.LENGTH_SHORT).show()
                }
            } else {
                Const.uploadLocation = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val view = it as ImageView
                    view.drawable.setTint(resources.getColor(R.color.colorAccent))
                } else {
                    Toast.makeText(this, "开始上传定位数据", Toast.LENGTH_SHORT).show()
                }
            }
        }
        main_tool_op.setOnClickListener {
            if (isExpand) {
                //收缩操作
                isExpand = false
                main_tool_op.setImageResource(R.drawable.ic_unfold)
                val y = main_user_layout.height - main_user_tool.height
                main_user_layout.animate().y((-y).toFloat()).setInterpolator(AccelerateInterpolator()).duration = 500
            } else {
                //展开操作
                isExpand = true
                main_tool_op.setImageResource(R.drawable.ic_packup)
                main_user_layout.animate().y(0f).setInterpolator(BounceInterpolator()).duration = 500
            }
        }

        move_my_locate.setOnClickListener {
            controller?.locateMyPosition()
        }

        main_user_layout.setOnClickListener {
            Log.i("MainActivity", "initView:只为了拦截点击事件")
        }

        if (SPUtil.checkUpdate()) {
            checkUpdate()
        }

        queryIsHelp()
    }

    private fun queryIsHelp() {
        MessageManager.addReceiver(99){
            runOnUiThread {
                AlertDialog.Builder(this).setTitle("求助！").setMessage("id为{$it}需要您的帮助")
                    .setPositiveButton("开始追踪！"){_,_->
                        Const.isHelp = true
                        controller?.startNewHelp(1,it)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            main_user_alert.drawable.setTint(resources.getColor(R.color.colorAccent))
                        }
                    }.setNegativeButton("取消",null)
                    .show()
            }
        }
    }

    private fun checkUpdate() {
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).getLastVer(Utils.getAppVersionName(this))).handler(object : ResultHandler {
            override fun onSuccess(result: Result?) {
                val bean = Gson().fromJson(result?.extra, VersionBean::class.java)
                AlertDialog.Builder(this@MainActivity).setTitle("有新版本发布").setMessage("是否更新").setPositiveButton(R.string.sure) { _, _ ->
                    Const.downLoadFile = true
                    val d1 = NetWorkUtils.NetApi().api(API::class.java).downloadFile(bean.url).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe { body ->
                        Toast.makeText(this@MainActivity, "正在下载...", Toast.LENGTH_SHORT).show()
                        Utils.writeFile2Disk(body, object : DownloadListener {
                            override fun onProgress(progress: Int) {

                            }

                            override fun onFinish() {
                                Const.downLoadFile = false
                                val i = Intent(Intent.ACTION_VIEW)
                                val filePath = Environment.getExternalStorageDirectory().path + "/随行/download/lastVersion.apk"
                                i.setDataAndType(
                                    Uri.parse("file://$filePath"), "application/vnd.android.package-archive"
                                )
                                if ((Build.VERSION.SDK_INT >= 24)) { //判读版本是否在7.0以上
                                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                        })
                    }
                }.setNegativeButton(R.string.cancel) { _, _ ->
                    SPUtil.saveUpdateAppOpreation()
                }.show()
            }

            override fun onFailed(e: String?) {
                SPUtil.saveUpdateAppOpreation()
            }
        })
    }

    override fun receiver(msg: String?) {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                main_user_urgent.drawable.setTint(resources.getColor(R.color.colorAccent))
            }
            if (!Const.uploadLocation) {
                main_user_upload_location.performClick()
            }
            Toast.makeText(this, "紧急联系中~", Toast.LENGTH_SHORT).show()
            SPUtil.setAccessLocate(false)
            var code = Gson().fromJson(msg, Result::class.java).extra
            if (code.length > 4) {
                code = code.substring(1, 5) + "\n紧急联系人不在线，请通过其他方式紧急联系"
            }
            AlertDialog.Builder(this).setTitle("求助").setMessage(
                "已发送相关通知，您也可以将您的求助码通过其他软件发给其他人让除紧急人(包含)帮助您。求助码:$code"
            ).setPositiveButton("拨号") { _, _ ->
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${SPUtil.getEmergency()}")
                startActivity(intent)
            }.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        controller?.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        controller!!.onResume()
        if (null == SPUtil.getUser()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        main_user_nick.text = SPUtil.getUser().name
        Glide.with(this).setDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_icon)).load(SPUtil.getUser().avatar).into(main_user_avatar)
    }

    override fun onPause() {
        super.onPause()
        controller!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller!!.onDestroy()
        EventBus.getDefault().unregister(this)
        ChatModuleManager.close()
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public fun onLocationEvent(event: MyLocationEvent) {
        if (mAddress?.text.toString() != event.addr) {
            mAddress?.text = event.addr
            if (!isExpand) {
                Handler().postDelayed({
                    runOnUiThread {
                        val y = main_user_layout.height - main_user_tool.height
                        main_user_layout.animate().y((-y).toFloat()).setInterpolator(AccelerateInterpolator()).duration = 0
                    }
                }, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TRACE_HISTORY_CODE) {
            if (resultCode == RESULT_OK) {
                controller?.setTrace(
                    SPUtil.getUser().id, data?.getStringExtra("start"), data?.getStringExtra("end")
                )
            }
        }
    }

    override fun showTips(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun overTrace() {
        controller?.noCenter()
    }

}
