package com.chandra.syncnote.util

sealed class OrderType{
    object Ascending : OrderType()
    object Descending : OrderType()
}