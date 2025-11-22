package to.msn.wings.carmaintenancerecord.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.local.entity.CarEntity

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: CarEntity): Long

    @Update
    suspend fun update(car: CarEntity)

    @Query("SELECT * FROM cars WHERE id = :carId")
    suspend fun getCar(carId: Long): CarEntity?

    @Query("SELECT * FROM cars WHERE id = :carId")
    fun getCarFlow(carId: Long): Flow<CarEntity?>

    @Query("SELECT * FROM cars LIMIT 1")
    suspend fun getFirstCar(): CarEntity?

    @Query("SELECT * FROM cars LIMIT 1")
    fun getFirstCarFlow(): Flow<CarEntity?>

    @Query("DELETE FROM cars WHERE id = :carId")
    suspend fun deleteCar(carId: Long)
}
