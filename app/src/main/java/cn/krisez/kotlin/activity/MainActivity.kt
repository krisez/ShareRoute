package cn.krisez.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyTrafficStyle

import cn.krisez.shareroute.base.CheckPermissionsActivity
import cn.krisez.shareroute.maps.MapController
import cn.krisez.shareroute.maps.MarkerInfoWindow
import cn.krisez.network.NetWorkUtils
import cn.krisez.kotlin.net.API
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.bean.TrackPoint
import cn.krisez.shareroute.bean.User
import cn.krisez.shareroute.utils.SPUtil
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.google.gson.Gson

class MainActivity : CheckPermissionsActivity() {

    private var controller: MapController? = null
    private var mMarkerInfoWindow: MarkerInfoWindow? = null

    private var mLocateO: ImageButton? = null
    private var mMapView: MapView? = null
    private var mEditTextInfo: EditText? = null
    private var mConstraintLayout: ConstraintLayout? = null

    private var mAMap: AMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mMapView = findViewById(R.id.mv_show_me_other)
        mLocateO = findViewById(R.id.ib_locate_other)
        mEditTextInfo = findViewById(R.id.et_other_info_id)
        mConstraintLayout = findViewById(R.id.csl_getInfo)

        //得到另外人的信息
        findViewById<Button>(R.id.btn_get_other_info).setOnClickListener { getOthersInfo() }

        mLocateO!!.visibility = View.GONE

        controller = MapController(this)
        controller!!.map(mMapView).defaultAmap().create(savedInstanceState)

        mAMap = mMapView!!.map
        val myTrafficStyle = MyTrafficStyle()
        myTrafficStyle.seriousCongestedColor = Color.parseColor("#790000")
        myTrafficStyle.congestedColor = Color.parseColor("#ff0000")
        myTrafficStyle.slowColor = Color.parseColor("#a9c433")
        myTrafficStyle.smoothColor = Color.parseColor("#5c71fc71")
        mAMap!!.myTrafficStyle = myTrafficStyle
        mAMap!!.isTrafficEnabled = true

        mMarkerInfoWindow = MarkerInfoWindow(this)
        mAMap!!.setInfoWindowAdapter(mMarkerInfoWindow)

        mAMap!!.setOnMarkerClickListener {
            Toast.makeText(this@MainActivity, it.title, Toast.LENGTH_SHORT).show()
            true
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
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).getOtherPos(SPUtil.getOtherInfo()))
            .handler(object : ResultHandler {
                override fun onSuccess(result: Result?) {
                    val s = result?.extra
                    Log.d("MainActivity", "onSuccess:$s")
                    val point = Gson().fromJson(s, TrackPoint::class.java)
                    val lat = java.lang.Double.parseDouble(point.lat)
                    val lng = java.lang.Double.parseDouble(point.lng)
                    val direction = java.lang.Float.parseFloat(point.direction)
                    val latLng = LatLng(lat,lng)
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

    override fun onResume() {
        super.onResume()
        controller!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        controller!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller!!.onDestroy()
    }
}
