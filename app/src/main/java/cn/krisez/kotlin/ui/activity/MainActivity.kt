package cn.krisez.kotlin.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.*
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
import cn.krisez.shareroute.bean.TrackPoint
import cn.krisez.shareroute.bean.UrgentBean
import cn.krisez.shareroute.event.MyLocationEvent
import cn.krisez.shareroute.maps.MapController
import cn.krisez.shareroute.maps.MarkerInfoWindow
import cn.krisez.shareroute.ui.LoadFragment
import cn.krisez.shareroute.utils.Const
import cn.krisez.shareroute.utils.SPUtil
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyTrafficStyle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MainActivity : CheckPermissionsActivity(), IMapView,MessageReceiver {

    private var TRACE_HISTORY_CODE = 701
    private var isExpand = true

    private var controller: MapController? = null
    private var mMarkerInfoWindow: MarkerInfoWindow? = null

    private var mLocateO: ImageButton? = null
    private var mMapView: MapView? = null
    private var mEditTextInfo: EditText? = null
    private var mConstraintLayout: ConstraintLayout? = null
    private var mAddress: TextView? = null

    private var mAMap: AMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        ImConst.id = SPUtil.getUser().id
        startService(
            Intent(this, IMMsgService::class.java).putExtra(
                "user",
                SPUtil.getUser().toString()
            ).putExtra("cls", MainActivity::class.java)
        )
        initView(savedInstanceState)
        initMap(savedInstanceState)
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mLocateO?.visibility = View.GONE

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
        MessageManager.addReceiver(0) {
            Log.d("MainActivity", "initMap:$it")
        }
    }

    private fun initView(savedInstanceState: Bundle?) {
        mMapView = findViewById(R.id.map_view)
        mLocateO = findViewById(R.id.ib_locate_other)
        mEditTextInfo = findViewById(R.id.et_other_info_id)
        mConstraintLayout = findViewById(R.id.csl_getInfo)
        mAddress = findViewById(R.id.main_user_address)
        val uploadLocation = findViewById<ImageView>(R.id.main_user_upload_location)
        val layoutOperation = findViewById<ImageView>(R.id.main_tool_op)
        //得到另外人的信息
        findViewById<Button>(R.id.btn_get_other_info).setOnClickListener { getOthersInfo() }

        main_user_id.text = SPUtil.getUser().id
        main_user_avatar.setOnClickListener {
            startActivity(Intent(this, PersonalActivity::class.java))
        }
        main_user_setup.setOnClickListener {
            startActivity(Intent(this, LoadFragment::class.java).putExtra("cls", "set"))
        }
        main_user_help.setOnClickListener {}
        main_user_urgent.setOnClickListener {
            MessageManager.send(Gson().toJson(WebSocketTransfer(77,Gson().toJson(UrgentBean(SPUtil.getUser().id, SPUtil.getEmergency(), SPUtil.getUser().realName)))))
            if (!Const.uploadLocation) {
                uploadLocation.performClick()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val view = it as ImageView
                view.drawable.setTint(resources.getColor(R.color.vector_reset))
            }
            Toast.makeText(this, "紧急联系中~", Toast.LENGTH_SHORT).show()
            MessageManager.addReceiver(77,this)
        }
        main_user_message.setOnClickListener {
            ChatModuleManager.open(this, SPUtil.getUser().toString())
            main_msg_tips_dot.visibility = View.INVISIBLE
        }
        main_user_history.setOnClickListener {
            startActivityForResult(
                Intent(this, TraceHistoryActivity::class.java),
                TRACE_HISTORY_CODE
            )
        }
        main_user_mail.setOnClickListener {}
        uploadLocation.setOnClickListener {
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
        layoutOperation.setOnClickListener {
            if (isExpand) {
                //收缩操作
                isExpand = false
                layoutOperation.setImageResource(R.drawable.ic_unfold)
                val y = main_user_layout.height - main_user_tool.height
                main_user_layout.animate().y((-y).toFloat())
                    .setInterpolator(AccelerateInterpolator()).duration = 500
            } else {
                //展开操作
                isExpand = true
                layoutOperation.setImageResource(R.drawable.ic_packup)
                main_user_layout.animate().y(0f).setInterpolator(BounceInterpolator()).duration =
                    500
            }
        }

        move_my_locate.setOnClickListener {
            controller?.locateMyPosition()
        }

        main_user_layout.setOnClickListener {
            Log.d("MainActivity", "initView:只为了拦截点击事件")
        }
    }

    override fun receiver(msg: String?) {
        runOnUiThread{
            AlertDialog.Builder(this).setTitle("求助").setMessage("已发送相关通知，您也可以将您的求助码通过其他软件发给其他人让除紧急人(包含)帮助您。求助码:"+Gson().fromJson(msg,Result::class.java).extra).show()
        }
    }

    //得到另一个人的info
    private fun getOthersInfo() {
        val s = mEditTextInfo!!.text.toString()
        if (s != "") {
            SPUtil.setOtherInfo(s)
            mConstraintLayout!!.visibility = View.GONE
            mLocateO!!.visibility = View.VISIBLE
        }
    }

    fun locateOthers(view: View) {
        NetWorkUtils.INSTANCE()
            .create(NetWorkUtils.NetApi().api(API::class.java).getOtherPos(SPUtil.getOtherInfo()))
            .handler(object : ResultHandler {
                override fun onSuccess(result: Result?) {
                    val s = result?.extra
                    val point = Gson().fromJson(s, TrackPoint::class.java)
                    val lat = point.lat.toDouble()
                    val lng = point.lng.toDouble()
                    val direction = point.direction.toFloat()
                    val latLng = LatLng(lat, lng)
                    mAMap!!.animateCamera(CameraUpdateFactory.changeLatLng(latLng))
                    controller!!.setMarkerOption(
                        MarkerOptions().title(SPUtil.getOtherInfo())
                            .rotateAngle(direction)
                            .position(latLng)
                    )
                }

                override fun onFailed(msg: String?) {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }

            })
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
        Glide.with(this).setDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_icon))
            .load(SPUtil.getUser().avatar).into(main_user_avatar)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onLocationEvent(event: MyLocationEvent) {
        if (mAddress?.text.toString() != event.addr) {
            mAddress?.text = event.addr
            if (!isExpand) {
                Handler().postDelayed({
                    runOnUiThread {
                        val y = main_user_layout.height - main_user_tool.height
                        main_user_layout.animate().y((-y).toFloat())
                            .setInterpolator(AccelerateInterpolator()).duration = 0
                    }
                }, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TRACE_HISTORY_CODE) {
            if (resultCode == RESULT_OK) {
//                controller?.setTrace("1937821",data?.getStringExtra("start"),data?.getStringExtra("end"))
                controller?.setTrace(
                    SPUtil.getUser().id,
                    data?.getStringExtra("start"),
                    data?.getStringExtra("end")
                )
            }
        }
    }

    override fun showTips(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun overTrace() {
        controller?.noCenter()
        Log.d("MainActivity", "overTrace:？??")
    }

}
