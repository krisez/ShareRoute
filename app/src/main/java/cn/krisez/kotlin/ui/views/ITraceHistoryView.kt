package cn.krisez.kotlin.ui.views

import cn.krisez.framework.base.IBaseView
import cn.krisez.shareroute.bean.TraceHistoryBean

interface ITraceHistoryView : IBaseView {
    fun traceHistory(traces:List<TraceHistoryBean>)
}