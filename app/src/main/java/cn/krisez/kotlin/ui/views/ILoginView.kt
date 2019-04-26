package cn.krisez.kotlin.ui.views

import cn.krisez.framework.base.IBaseView

interface ILoginView: IBaseView {
    fun showTips(tips:String)
    fun loginSuccess()
    fun registerSuccessful()
    fun code(code:String)
}