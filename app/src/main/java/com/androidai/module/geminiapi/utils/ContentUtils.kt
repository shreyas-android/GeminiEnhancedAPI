package com.androidai.module.geminiapi.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.androidai.module.geminiapi.R
import java.util.Random

object ContentUtils {

    fun shareContent(context: Context, data: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        val title = context.getString(R.string.label_share_content)
        val chooser = Intent.createChooser(intent, title)
        context.startActivity(chooser)
    }

    private fun copyContentToClipboard(context: Context, content: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(Random().nextLong().toString(), content)
        clipboard.setPrimaryClip(data)
    }

    fun copyAndShowToast(context: Context, result: String) {
        copyContentToClipboard(
            context = context,
            content = result
        )
    }
}