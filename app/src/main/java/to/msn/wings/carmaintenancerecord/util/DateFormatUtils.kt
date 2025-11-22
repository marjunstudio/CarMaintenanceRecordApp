package to.msn.wings.carmaintenancerecord.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * タイムスタンプを指定されたフォーマットで文字列に変換する
 *
 * @param timestamp Unix時間（ミリ秒）
 * @param pattern 日付フォーマットパターン（デフォルト: "yyyy/MM/dd HH:mm"）
 * @param locale ロケール（デフォルト: システムデフォルト）
 * @return フォーマットされた日付文字列
 *
 * @sample
 * ```
 * val formatted = formatTimestamp(1234567890000L) // "2009/02/14 08:31"
 * val customFormat = formatTimestamp(1234567890000L, "yyyy年MM月dd日") // "2009年02月14日"
 * ```
 */
fun formatTimestamp(
    timestamp: Long,
    pattern: String = "yyyy/MM/dd HH:mm",
    locale: Locale = Locale.getDefault()
): String {
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(Date(timestamp))
}