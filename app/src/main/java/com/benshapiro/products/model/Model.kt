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
    val price: Double,
    // Although this int could be any number, in the app it just goes from 0 to 1 and back to 0 etc
    val bookmark: Int = 0
) : Serializable

// This fun is so that when the price is displayed it looks like a price not just a number
fun Model.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.UK).format(price)
// Serializable is needed to allow the data to be converted by the app so that it can be sent into a database or sent by a network.
// Although I believe there are other ways of achieving this communication, this is tremendously simple.