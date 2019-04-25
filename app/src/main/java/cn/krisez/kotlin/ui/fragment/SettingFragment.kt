package cn.krisez.kotlin.ui.fragment

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import cn.krisez.imchat.ChatModuleManager
import cn.krisez.shareroute.R
import cn.krisez.shareroute.utils.GlideCacheUtil
import cn.krisez.shareroute.utils.SPUtil
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SPUtil.getEmergency() == "") {
            view.set_emergency_contact.text = "无"
        } else {
            view.set_emergency_contact.text = SPUtil.getEmergency()
        }
        view.set_emergency.setOnClickListener {
            val editText = EditText(context)
            context?.let { c ->
                AlertDialog.Builder(c).setTitle("设置您的紧急联系人号码").setView(editText)
                    .setPositiveButton(R.string.sure) { _, _ ->
                        if (editText.text.toString() == "" || editText.text.toString().length != 11) {
                            Toast.makeText(c, "输入有误！", Toast.LENGTH_SHORT).show()
                        } else {
                            SPUtil.setEmetgency(editText.text.toString())
                            view.set_emergency_contact.text = editText.text.toString()
                        }
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        }
        view.set_access_locate.isChecked = SPUtil.isAccessLocate()
        view.set_access_locate.setOnCheckedChangeListener { _, isChecked ->
            SPUtil.setAccessLocate(isChecked)
        }
        view.set_clear_img_size.text = GlideCacheUtil.getInstance().getCacheSize(context)
        view.set_clear.setOnClickListener {
            GlideCacheUtil.getInstance().clearImageAllCache(context)
            view.set_clear_img_size.text = GlideCacheUtil.getInstance().getCacheSize(context)
        }
        view.set_clear_msg.setOnClickListener {
            ChatModuleManager.clearMsg(context)
        }
    }
}