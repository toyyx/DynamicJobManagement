package com.example.dynamicjobmanagement.model.model

class User (
    var account: String,//学号或工号
    var password: String,
    var name: String){
    //无参构造函数
    constructor() : this("", "", "",)
}
