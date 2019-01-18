package cn.krisez.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import cn.krisez.kotlin.net.API
import cn.krisez.network.NetWorkUtils
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.base.BaseActivity
import cn.krisez.shareroute.client.ImClient
import cn.krisez.shareroute.presenter.Presenter
import cn.krisez.shareroute.utils.SPUtil

class LoginActivity : BaseActivity() {
    override fun newView(): View {
        return View.inflate(this, R.layout.activity_login, null)
    }

    override fun presenter(): Presenter? {
        return null
    }

    override fun init(bundle: Bundle?) {

        findViewById<ImageView>(R.id.iv_login_sign_in).setOnClickListener{
            val constraintLayout = layoutInflater.inflate(R.layout.window_login,null)
            val popupWindow = PopupWindow(constraintLayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.isFocusable = true
            popupWindow.showAtLocation(it,Gravity.CENTER,0,-150)
            popupWindow.setOnDismissListener {

            }
        }

        findViewById<ImageView>(R.id.iv_login_sign_up).setOnClickListener{
            val constraintLayout = layoutInflater.inflate(R.layout.window_register,null)
            val popupWindow = PopupWindow(constraintLayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.isFocusable = true
            popupWindow.showAtLocation(it,Gravity.CENTER,0,-150)
            popupWindow.setOnDismissListener {

            }
        }

        if(SPUtil.getUserId() != ""){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun startLoginAndGo(ss: String) {
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).login(ss))
                .handler(object : ResultHandler{
                    override fun onSuccess(result: Result?) {
                        if(result?.statue == "ok"){
                            SPUtil.saveUserId(ss)
                            ImClient.getInstance("ws://192.168.238.252:932/$ss").connect()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        toast(msg)
                    }

                })
    }

    override fun onRefresh() {
        toast("傻了？")
    }
}
