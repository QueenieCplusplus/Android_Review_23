package com.katepatty.kateswifi.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.katepatty.kateswifi.R


// IPC & INTENT
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter

// WIFI & CONTEXT
import android.net.wifi.WifiManager
import android.content.Context

// WIDGET
import android.widget.Switch
import android.widget.Toast

class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    lateinit var wifiSwitch: Switch
    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setSt(arguments!!.getString("gg", "validate"))
        }


    }


    // all view display in this phase
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        val textView: TextView = root.findViewById(R.id.section_label)

        // Tab 功能 -------------------------
        pageViewModel.text.observe(this, Observer<String> {
            textView.text = it
        })


        /*
        * 1. You can directly use the activity or context reference in your fragment.

        * So instead of using this and applicationContext use getActivity() ot getContext() method.

        * 2. remove the setContentView() from on create as its of no need cause you are already inflating the view properly in onCreateView().
        * */


        // SYS WIFI
        // activity 無須 root, 僅 fragment 需要
        wifiSwitch = root.findViewById(R.id.wifiSwitch)
        // activity 無須 context, 僅 fragment 需要
        wifiManager = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifiSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //wifiManager.connectionInfo
                //wifiManager.dhcpInfo
                wifiManager.isWifiEnabled = true
                wifiSwitch.text = "WiFi is opened"
            } else {
                wifiManager.isWifiEnabled = false
                wifiSwitch.text = "WiFi is closed"
            }
        }



        return root



    }


    // background -> foreground
    override fun onStart() {

        super.onStart()

        // activity 無須 context, 僅 fragment 需要
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        context!!.registerReceiver(wifiStateReceiver, intentFilter)
    }

    // foreground -> background
    override fun onStop() {

        super.onStop()

        // activity 無須 context, 僅 fragment 需要
        context!!.unregisterReceiver(wifiStateReceiver)
    }


    private val wifiStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            when (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN)) {

                WifiManager.WIFI_STATE_ENABLED -> {
                    wifiSwitch.isChecked = true
                    wifiSwitch.text = "WiFi is ON"
                    Toast.makeText(getActivity(), "Wifi is already On in background", Toast.LENGTH_SHORT).show()
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    wifiSwitch.isChecked = false
                    wifiSwitch.text = "WiFi is OFF"
                    Toast.makeText(getActivity(), "Wifi is already Off in background", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Tab 功能 -------------------------

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_KEY = "gg"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNum: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_KEY, sectionNum)
                }
            }
        }
    }




}