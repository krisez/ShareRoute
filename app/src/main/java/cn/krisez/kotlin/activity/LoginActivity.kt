package cn.krisez.kotlin.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import cn.krisez.kotlin.net.API
import cn.krisez.network.NetWorkUtils
import cn.krisez.network.bean.Result
import cn.krisez.network.handler.ResultHandler
import cn.krisez.shareroute.R
import cn.krisez.shareroute.base.BaseActivity
import cn.krisez.shareroute.client.ImClient
import cn.krisez.shareroute.presenter.Presenter
import cn.krisez.shareroute.utils.SPUtil
import android.view.WindowManager
import android.widget.*
import cn.krisez.shareroute.presenter.LoginPresenter

class LoginActivity : BaseActivity(),ILoginView{

    private var lp: WindowManager.LayoutParams? = null
    private var popupWindow: PopupWindow? = null
    private var presenter: LoginPresenter? = null

    private val mHandler = Handler { msg ->
        backgroundAlpha(msg.obj as Float)
        false
    }

    override fun newView(): View {
        return View.inflate(this, R.layout.activity_login, null)
    }

    override fun presenter(): Presenter? {
        presenter = LoginPresenter(this,this)
        return presenter
    }

    override fun init(bundle: Bundle?) {
        findViewById<ImageView>(R.id.iv_login_sign_in).setOnClickListener {
            val animator = ValueAnimator.ofFloat(1f,0.5f)
            animator.duration = 1000
            animator.addUpdateListener {value->
                backgroundAlpha(value.animatedValue as Float)
            }
            animator.start()
            popupWindow(it,1)
        }

        findViewById<ImageView>(R.id.iv_login_sign_up).setOnClickListener {
            val animator = ValueAnimator.ofFloat(1f,0.5f)
            animator.duration = 1000
            animator.addUpdateListener {value->
                backgroundAlpha(value.animatedValue as Float)
            }
            animator.start()
            popupWindow(it,2)
        }

        if (SPUtil.getUserId() != "") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun startLoginAndGo(ss: String) {
        NetWorkUtils.INSTANCE().create(NetWorkUtils.NetApi().api(API::class.java).login(ss,"123"))
            .handler(object : ResultHandler {
                override fun onSuccess(result: Result?) {
                    if (result?.statue == "ok") {
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

    fun popupWindow(view: View, type: Int) {
        if (popupWindow != null && popupWindow!!.isShowing) {
            return
        }
        val id = if(type == 1) R.layout.window_login else R.layout.window_register
        val constraintLayout = layoutInflater.inflate(id, null)
        val popupWindow =
            PopupWindow(constraintLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.isFocusable = true
        //点击空白处时，隐藏掉pop窗口
        popupWindow.isFocusable = true
        //popupWindow.setBackgroundDrawable(BitmapDrawable())
        //添加弹出、弹入的动画
        popupWindow.animationStyle = R.style.pop
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -150)
        //添加按键事件监听
        setLayoutListener(constraintLayout,type)
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener {
            val animator = ValueAnimator.ofFloat(0.5f,1f)
            animator.duration = 500
            animator.addUpdateListener {
                backgroundAlpha(it.animatedValue as Float)
            }
            animator.start()
        }
    }

    private fun setLayoutListener(layout: View, type:Int) {
        val tvTips = layout.findViewById<TextView>(R.id.login_tips)
        if(type == 1){
            //登录
            val etAccount = layout.findViewById<EditText>(R.id.et_login_account)
            val etPassword = layout.findViewById<EditText>(R.id.et_login_password)
            layout.findViewById<Button>(R.id.btn_login).setOnClickListener {
                presenter
            }
        }else{
            //注册
            val etAccount = layout.findViewById<EditText>(R.id.register_account)
            val etCode = layout.findViewById<EditText>(R.id.register_code)
            layout.findViewById<Button>(R.id.btn_register).setOnClickListener {  }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    fun backgroundAlpha(bgAlpha: Float) {
        lp = window.attributes
        lp!!.alpha = bgAlpha
        window.attributes = lp
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun showTips(tips: String) {
        toast("请记住您的初始密码为[123456]！")
    }
}
