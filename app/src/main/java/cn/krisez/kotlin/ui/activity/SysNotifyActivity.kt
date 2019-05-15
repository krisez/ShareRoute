package cn.krisez.kotlin.ui.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import cn.krisez.kotlin.net.API
import cn.krisez.network.NetWorkUtils
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.adapter.NotifyAdapter
import cn.krisez.shareroute.bean.SysMailBean
import cn.krisez.shareroute.utils.SPUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_sys_notify.*
import java.util.ArrayList

class SysNotifyActivity : BaseActivity() {

    private var mAdapter:NotifyAdapter? = null

    override fun init(bundle: Bundle?) {
        setUpToolbar()
        showBackIconAndClick()
        setRefreshEnable(true)
        mAdapter = NotifyAdapter(R.layout.item_sys_mail,arrayListOf<SysMailBean>())
        rv_sys_notify.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_sys_notify.itemAnimator = DefaultItemAnimator()
        rv_sys_notify.adapter = mAdapter

        mAdapter?.setOnItemClickListener { a, _, p ->
            val list = a.data as List<SysMailBean>
            AlertDialog.Builder(this).setTitle(list[p].title).setMessage(list[p].content).setPositiveButton("了解", null).show()
        }
        onRefresh()
    }

    override fun onRefresh() {
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).getMail(SPUtil.getMailTime()))
            .handler(object :ResultHandler{
                override fun onSuccess(result: Result?) {
                    mAdapter?.setNewData(Gson().fromJson<List<SysMailBean>>(result?.extra, object : TypeToken<List<SysMailBean>>() {}.type))
                    disableRefresh()
                }

                override fun onFailed(s: String?) {
                    Toast.makeText(this@SysNotifyActivity,s,Toast.LENGTH_SHORT).show()
                    disableRefresh()
                }
            })
    }

    override fun newView(): View = View.inflate(this, R.layout.activity_sys_notify,null)

    override fun presenter(): Presenter? = null
}