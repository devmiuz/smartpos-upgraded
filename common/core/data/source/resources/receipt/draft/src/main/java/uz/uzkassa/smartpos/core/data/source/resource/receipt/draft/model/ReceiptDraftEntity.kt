package uz.uzkassa.smartpos.core.data.source.resource.receipt.draft.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_drafts")
data class ReceiptDraftEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receipt_draft_id")
    val id: Long,

    @ColumnInfo(name = "receipt_draft_receipt_uid")
    val receiptUid: String,

    @ColumnInfo(name = "receipt_draft_name")
    val name: String,

    @ColumnInfo(name = "receipt_draft_is_remote")
    val isRemote: Boolean
) {

    @Ignore
    constructor(
        receiptUid: String,
        name: String,
        isRemote: Boolean
    ) : this(
        id = 0,
        receiptUid = receiptUid,
        name = name,
        isRemote = isRemote
    )
}