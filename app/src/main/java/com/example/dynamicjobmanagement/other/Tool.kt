package com.example.dynamicjobmanagement.other

import org.json.JSONArray

object Tool {
    fun getListFromJSONArray(jsonArray: JSONArray):MutableList<String>{
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getString(i)
            list.add(item)
        }
        return list
    }


}