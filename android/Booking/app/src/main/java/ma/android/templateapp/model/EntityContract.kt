package ma.android.templateapp.model

import android.provider.BaseColumns

object EntityContract {
    const val DB_NAME = "booking_database"
    const val DB_VERSION = 1

    object TaskEntry : BaseColumns {
        const val DB_TABLE = "booking_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DOCTOR = "doctor"
        const val COLUMN_DATE = "date"
        const val COLUMN_HOUR = "hour"
        const val COLUMN_DETAILS = "details"
        const val COLUMN_STATUS = "status"

    }
}