package uz.uzkassa.smartpos.core.manager.logger.impl

import android.content.Context
import android.os.Build
import android.os.Environment
import uz.uzkassa.smartpos.core.manager.logger.LoggerTree
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class FileLoggerTree(private val context: Context) : LoggerTree(true) {

    override fun v(tag: String, msg: String?) =
        writeLog(PriorityColor.Verbose, tag, msg)

    override fun v(tag: String, msg: String?, tr: Throwable?) =
        writeLog(PriorityColor.Verbose, tag, msg, tr)

    override fun d(tag: String, msg: String?) =
        writeLog(PriorityColor.Debug, tag, msg)

    override fun d(tag: String, msg: String?, tr: Throwable?) =
        writeLog(PriorityColor.Debug, tag, msg, tr)

    override fun i(tag: String, msg: String?) =
        writeLog(PriorityColor.Info, tag, msg)

    override fun i(tag: String, msg: String?, tr: Throwable?) =
        writeLog(PriorityColor.Info, tag, msg, tr)

    override fun w(tag: String, msg: String?) =
        writeLog(PriorityColor.Warn, tag, msg)

    override fun w(tag: String, msg: String?, tr: Throwable?) =
        writeLog(PriorityColor.Warn, tag, msg)

    override fun w(tag: String, tr: Throwable?) =
        writeLog(PriorityColor.Warn, tag, null, tr)

    override fun e(tag: String, msg: String?) =
        writeLog(PriorityColor.Error, tag, msg)

    override fun e(tag: String, msg: String?, tr: Throwable?) =
        writeLog(PriorityColor.Error, tag, msg, tr)

    override fun wtf(tag: String, msg: String?) =
        writeLog(PriorityColor.Wtf, tag, msg)

    override fun wtf(tag: String, tr: Throwable?) =
        writeLog(PriorityColor.Wtf, tag, null, tr)

    override fun wtf(tag: String, msg: String?, tr: Throwable?) =
        writeLog(PriorityColor.Wtf, tag, msg, tr)

    private fun writeLog(
        color: PriorityColor,
        tag: String,
        message: String?,
        throwable: Throwable? = null
    ) {
        synchronized(this) {
            try {
                val fileTimeStamp: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val logTimeStamp: String =
                    SimpleDateFormat("HH:mm:ss:sss", Locale.getDefault()).format(Date())

                val file: File? = generateFile("$fileTimeStamp.html")

                if (file != null) {
                    val writer = FileWriter(file, true)
                    writer.append("<meta charset=\"UTF-8\">")
                        .append("<p style=\"background:${color.background};\">")
                        .append("<strong style=\"background:cornflowerblue;\">&nbsp&nbsp")
                        .append(logTimeStamp)
                        .append("&nbsp&nbsp</strong>")
                        .append("<strong>&nbsp&nbsp</strong>")
                        .append("<span style=\"color:${color.text}\">")
                        .append("$tag: ${message ?: ""}")
                        .append(getStackTraceString(throwable))
                        .append("</span></p>\n")
                    writer.flush()
                    writer.close()
                }
            } catch (ignore: Exception) {
            }
        }
    }

    private fun getStackTraceString(tr: Throwable?): String? {
        if (tr == null) return ""

        var throwable = tr
        while (throwable != null) {
            if (throwable is UnknownHostException) return ""
            throwable = throwable.cause
        }
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        tr.printStackTrace(printWriter)
        printWriter.flush()
        return stringWriter.toString()
    }

    @Suppress("DEPRECATION")
    private fun generateFile(fileName: String): File? {
        var file: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val absolutePath: String? = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    context.getExternalFilesDir(null)?.absolutePath
                else -> Environment.getExternalStorageDirectory().absolutePath
            }

            val root = File(absolutePath, "SmartPOS Trade" + File.separator + "Log")
            var dirExists = true
            if (!root.exists()) dirExists = root.mkdirs()
            if (dirExists) file = File(root, fileName)
        }
        return file
    }

    private sealed class PriorityColor(val background: String, val text: String) {
        object Debug : PriorityColor("dimgray", "#DCDCDC")
        object Error : PriorityColor("red", "#FFFFFF")
        object Info : PriorityColor("white", "#808080")
        object Verbose : PriorityColor("snow", "#808080")
        object Warn : PriorityColor("crimson", "#FFFFFF")
        object Wtf : PriorityColor("firebrick", "#FFFFFF")
    }
}