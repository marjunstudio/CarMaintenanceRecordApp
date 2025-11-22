package to.msn.wings.carmaintenancerecord.domain.model

/**
 * 車両情報を表すドメインモデル
 *
 * @property id 車両ID（0の場合は新規登録）
 * @property name 車両の愛称（例: 「マイカー」「通勤用」）
 * @property manufacturer メーカー名（例: 「トヨタ」「ホンダ」）
 * @property model 車種/モデル名（例: 「プリウス」「フィット」）
 * @property year 年式（西暦）
 * @property licensePlate ナンバープレート（任意）
 * @property initialMileage 納車時の走行距離（km）。新車の場合は0、中古車の場合は納車時の値
 * @property mileage 現在の走行距離（km）。所有してからの走行距離は [mileage] - [initialMileage] で計算可能
 * @property photoUri 車両の写真URI（任意）
 * @property createdAt 登録日時（Unixタイムスタンプ ミリ秒）
 * @property updatedAt 更新日時（Unixタイムスタンプ ミリ秒）
 */
data class Car(
    val id: Long = 0,
    val name: String,
    val manufacturer: String,
    val model: String,
    val year: Int,
    val licensePlate: String? = null,
    val initialMileage: Int = 0,
    val mileage: Int,
    val photoUri: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
