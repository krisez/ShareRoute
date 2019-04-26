package cn.krisez.kotlin.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.krisez.shareroute.R
import kotlinx.android.synthetic.main.fragment_about_question.view.*

class AboutAndQuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_about_question, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.version.text =getAppVersionName(context)
        view.fankui.setOnClickListener {

        }
    }

    private fun getAppVersionName(context: Context?):String?{
        var versionName:String? = null
        try{
            versionName = context?.packageManager?.getPackageInfo(context.packageName,0)?.versionName
        }catch (e:Exception){
            e.printStackTrace()
        }
        return "版本号：$versionName"
    }
}