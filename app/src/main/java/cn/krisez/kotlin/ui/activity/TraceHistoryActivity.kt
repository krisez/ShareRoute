package cn.krisez.kotlin.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.krisez.framework.base.BaseActivity
import cn.krisez.framework.base.Presenter
import cn.krisez.kotlin.ui.views.ITraceHistoryView
import cn.krisez.shareroute.R
import cn.krisez.shareroute.adapter.TraceAdapter
import cn.krisez.shareroute.bean.TraceHistoryBean
import cn.krisez.shareroute.presenter.TraceHistoryPresenter
import cn.krisez.shareroute.utils.SPUtil
import cn.krisez.shareroute.utils.Utils
import kotlinx.android.synthetic.main.activity_trace_history.*

class TraceHistoryActivity: BaseActivity(),ITraceHistoryView {

    private var mPresenter:TraceHistoryPresenter?=null
    private var mAdapter: TraceAdapter?=null
    override fun newView(): View = View.inflate(this, R.layout.activity_trace_history,null)

    override fun presenter(): Presenter? {
        mPresenter = TraceHistoryPresenter(this,this)
        return mPresenter
    }

    override fun init(bundle: Bundle?) {
        setRefreshEnable(true)
        setUpToolbar()
        showBackIconAndClick()
        mAdapter = TraceAdapter(R.layout.item_trace_history, arrayListOf())
        rv_trace_history.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_trace_history.adapter = mAdapter
        onRefresh()

        mAdapter?.setOnItemClickListener { a, _, p ->
            val bean = a.data[p] as TraceHistoryBean
            val start = bean.createTime
            val end = Utils.time2Add(start,bean.pasTime)
            val intent = Intent()
            intent.putExtra("start",start)
            intent.putExtra("end",end)
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    override fun onRefresh() {
        mPresenter?.getTraces(SPUtil.getUser().id)
//        mPresenter?.getTraces("1937821")
    }

    override fun traceHistory(traces:List<TraceHistoryBean>) {
        mAdapter?.setNewData(traces)
        disableRefresh()
    }

}