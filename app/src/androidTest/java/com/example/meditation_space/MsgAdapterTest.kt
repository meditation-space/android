package com.example.meditation_space

import org.junit.Assert

import org.junit.Before
import org.junit.Test

class MsgAdapterTest {

    @Before
    fun setUp() {
    }

    @Test
    fun getItemCount() {
        val msgList = listOf(
            Msg("Hello", Msg.TYPE_RECEIVED),
            Msg("Hi", Msg.TYPE_SENT),
            Msg("Hey", Msg.TYPE_RECEIVED)
        )
        val adapter = MsgAdapter(msgList)

        Assert.assertEquals(msgList.size, adapter.getItemCount())
    }
}