package cn.krisez.kotlin.ui.activity

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.*
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import cn.krisez.kotlin.ui.views.ILoginView
import cn.krisez.shareroute.R
import cn.krisez.shareroute.presenter.LoginPresenter
import cn.krisez.shareroute.utils.SPUtil

class LoginActivity : BaseActivity(), ILoginView {

    private var lp: WindowManager.LayoutParams? = null
    private var popupWindow: PopupWindow? = null
    private var presenter: LoginPresenter? = null

    override fun newView(): View {
        return View.inflate(this, R.layout.activity_login, null)
    }

    override fun presenter(): Presenter? {
        presenter = LoginPresenter(this, this)
        return presenter
    }

    override fun init(bundle: Bundle?) {

        findViewById<ImageView>(R.id.iv_login_sign_in).setOnClickListener {
            val animator = ValueAnimator.ofFloat(1f, 0.5f)
            animator.duration = 1000
            animator.addUpdateListener { value ->
                backgroundAlpha(value.animatedValue as Float)
            }
            animator.start()
            popupWindow(it, 1)
        }

        findViewById<ImageView>(R.id.iv_login_sign_up).setOnClickListener {
            val animator = ValueAnimator.ofFloat(1f, 0.5f)
            animator.duration = 1000
            animator.addUpdateListener { value ->
                backgroundAlpha(value.animatedValue as Float)
            }
            animator.start()
            popupWindow(it, 2)
        }

    }

    override fun onRefresh() {
        toast("")
    }

    private fun popupWindow(view: View, type: Int) {
        if (popupWindow != null && popupWindow!!.isShowing) {
            return
        }
        val id = if (type == 1) R.layout.window_login else R.layout.window_register
        val constraintLayout = layoutInflater.inflate(id, null)
        popupWindow =
            PopupWindow(
                constraintLayout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        popupWindow?.isFocusable = true
        //点击空白处时，隐藏掉pop窗口
        popupWindow?.isFocusable = true
        popupWindow?.setBackgroundDrawable(BitmapDrawable())
        //添加弹出、弹入的动画
        popupWindow?.animationStyle = R.style.pop
        popupWindow?.showAtLocation(view, Gravity.CENTER, 0, -150)
        //添加按键事件监听
        setLayoutListener(constraintLayout, type)
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow?.setOnDismissListener {
            val animator = ValueAnimator.ofFloat(0.5f, 1f)
            animator.duration = 500
            animator.addUpdateListener {
                backgroundAlpha(it.animatedValue as Float)
            }
            animator.start()
        }
    }

    private var tvTips: TextView? = null
    private fun setLayoutListener(layout: View, type: Int) {
        tvTips = layout.findViewById(R.id.login_tips)
        if (type == 1) {
            //登录
            val etAccount = layout.findViewById<EditText>(R.id.et_login_account)
            val etPassword = layout.findViewById<EditText>(R.id.et_login_password)
            layout.findViewById<Button>(R.id.btn_login).setOnClickListener {
                presenter?.login(etAccount.text.toString(), etPassword.text.toString())
            }
        } else {
            //注册
            val etAccount = layout.findViewById<EditText>(R.id.register_account)
            val etCode = layout.findViewById<EditText>(R.id.register_code)
            val tvCode = layout.findViewById<TextView>(R.id.register_get_code)
            tvCode.setOnClickListener {
                if (etAccount?.text.toString().length != 11) {
                    tvTips?.text = "手机号有错误！"
                    tvTips?.visibility = View.VISIBLE
                } else {
                    presenter?.sendSMDCode(etAccount.text.toString())
                    val animator = ValueAnimator.ofInt(60, 0)
                    animator.duration = 60000
                    animator.interpolator = LinearInterpolator()
                    animator.addUpdateListener { value ->
                        val s = "剩下${value.animatedValue}s"
                        tvCode.text = s
                        if (value.animatedValue == 0) {
                            tvCode.setTextColor(resources.getColor(R.color.colorAccent))
                            tvCode.isClickable = true
                            tvCode.setText(R.string.get_verification_code)
                        }
                    }
                    animator.start()
                    tvCode.setTextColor(Color.GRAY)
                    tvCode.isClickable = false
                }
            }
            layout.findViewById<Button>(R.id.btn_register).setOnClickListener {
                if (mCode == etCode.text.toString()) {
                    presenter!!.register(etAccount.text.toString())
                } else {
                    tvTips?.visibility = View.VISIBLE
                    tvTips?.text = "验证码错误！"
                }
            }
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
        tvTips!!.visibility = View.VISIBLE
        tvTips?.text = tips
    }

    override fun loginSuccess() {
        toMain()
    }

    override fun registerSuccessful() {
        val editText = EditText(this)
        editText.hint = "默认密码为：123456"
        AlertDialog.Builder(this).setTitle("请设置您的登录密码")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("确定") { _, _ ->
                if ("" == editText.text.toString()) {
                    error("未设置密码")
                    toMain()
                } else {
                    presenter!!.updatePw(editText.text.toString())
                }
            }
            .setNegativeButton("跳过") { _, _ ->
                error("未设置密码")
                toMain()
            }
            .show()
    }

    private var mCode = "0000"
    override fun code(code: String) {
        mCode = code
    }

    private fun toMain() {
        if (popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
