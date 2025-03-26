package com.dicoding.asclepius.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class HistoryPrediction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "prediction")
    var prediction: String? = null,

    @ColumnInfo(name = "confidence_score")
    var confidence_score: String? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,
) : Parcelable
