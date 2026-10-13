package com.example.personalmessageassistant

import android.app.Notification
import android.app.RemoteInput
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

data class NotificationPreview(
    val appLabel: String,
    val title: String,
    val text: String,
    val hasReplyAction: Boolean
)

class MessageNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return

        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
        val text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?.takeIf { it.isNotBlank() }
            ?: extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()

        if (title.isBlank() && text.isBlank()) return

        val replyAction = notification.actions.orEmpty().any { action ->
            action.remoteInputs.orEmpty().any { input ->
                input.allowFreeFormInput || input.choices?.isNotEmpty() == true
            }
        }

        val appLabel = try {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(sbn.packageName, 0)
            ).toString()
        } catch (_: Exception) {
            sbn.packageName
        }

        synchronized(recent) {
            recent.add(
                0,
                NotificationPreview(
                    appLabel = appLabel,
                    title = title,
                    text = text,
                    hasReplyAction = replyAction
                )
            )
            while (recent.size > 30) recent.removeAt(recent.lastIndex)
        }
    }

    companion object {
        private val recent = mutableListOf<NotificationPreview>()

        fun recentNotifications(): List<NotificationPreview> =
            synchronized(recent) { recent.toList() }
    }
}
