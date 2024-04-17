package uz.uzkassa.smartpos.core.data.source.resource.card.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.card.model.cardtype.CardTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class CardTypeEntityDao : BaseDao.Impl<CardTypeEntity>() {

    @Query(value = "SELECT * FROM card_types")
    abstract fun getEntities(): Flow<List<CardTypeEntity>>

    @Query(value = "SELECT * FROM card_types WHERE card_type_id = :cardTypeId")
    abstract fun getEntitiesByCardTypeId(cardTypeId: Long): Flow<CardTypeEntity>
}