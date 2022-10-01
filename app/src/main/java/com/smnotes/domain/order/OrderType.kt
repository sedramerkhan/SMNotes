package com.smnotes.domain.order

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
