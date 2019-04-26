package cn.krisez.kotlin.ui.views

import cn.krisez.framework.base.IBaseView

interface IPersonView : IBaseView {
    fun code(code: String)
    fun uploadHead(avatar: String)
}
