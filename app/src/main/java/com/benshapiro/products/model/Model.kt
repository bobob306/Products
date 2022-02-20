package com.benshapiro.products.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.NumberFormat
import java.util.*

// Entity turns it into a database table, the table name is required
@Entity(tableName = "table_product")
data class Model(
    // Primary key allows the item to uniquely identified, this is also a column, just a more important one
    // This is required for a Room/SQLite database
    @PrimaryKey(autoGenerate = false)
    // each following line is just a column in the database
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val price: Double
) : Serializable

fun Model.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.UK).format(price)
// Serializable is needed to allow the data to be converted by the app so that it can be sent into a database or sent by a network.
// Although I believe there are other ways of achieving this communication, this is tremendously simple.