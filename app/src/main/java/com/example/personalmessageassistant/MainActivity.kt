package com.example.personalmessageassistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var feed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 28, 28, 28)
        }

        val title = TextView(this).apply {
            text = "Personal Message Assistant"
            textSize = 24f
        }
        status = TextView(this).apply {
            textSize = 15f
            setPadding(0, 16, 0, 16)
        }
        val accessButton = Button(this).apply {
            text = "Open notification access settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        val refreshButton = Button(this).apply {
            text = "Refresh notifications"
            setOnClickListener { refreshFeed() }
        }
        feed = TextView(this).apply {
            textSize = 15f
            text = "No notification previews captured yet."
        }

        root.addView(title)
        root.addView(status)
        root.addView(accessButton, matchWidth())
        root.addView(refreshButton, matchWidth())
        val scroll = ScrollView(this)
        scroll.addView(feed)
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
        refreshFeed()
    }

    private fun updateStatus() {
        val enabled = NotificationAccess.isEnabled(this)
        status.text = if (enabled) {
            "Notification access: enabled. Previews visible to Android may be captured."
        } else {
            "Notification access: off. Enable it only if you want this app to read notification previews."
        }
    }

    private fun refreshFeed() {
        val items = MessageNotificationListener.recentNotifications()
        feed.text = if (items.isEmpty()) {
            "No notification previews captured yet. Enable access, then wait for a new notification."
        } else {
            items.joinToString(separator = "\n\n──────────────\n\n") {
                "${it.appLabel}\n${it.title}\n${it.text}\nReply action available: ${if (it.hasReplyAction) "possibly yes" else "not detected"}"
            }
        }
    }

    private fun matchWidth() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

object NotificationAccess {
    fun isEnabled(activity: Activity): Boolean {
        val enabled = Settings.Secure.getString(
            activity.contentResolver, "enabled_notification_listeners"
        ) ?: return false
        return enabled.contains(activity.packageName)
    }
}
