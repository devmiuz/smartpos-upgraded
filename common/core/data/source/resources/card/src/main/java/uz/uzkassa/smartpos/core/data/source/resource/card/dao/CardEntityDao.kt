package uz.uzkassa.smartpos.core.data.source.resource.card.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardEntity
import uz.uzkassa.smartpos.core.data.source.resource.card.model.CardRelation
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao

@Dao
abstract class CardEntityDao : BaseDao.Impl<CardEntity>() {

    @Query(value = "SELECT * FROM cards INNER JOIN card_types ON card_card_type_id = card_type_id LEFT JOIN customers ON card_customer_id = customer_id ")
    abstract fun getRelations(): Flow<CardRelation>
}