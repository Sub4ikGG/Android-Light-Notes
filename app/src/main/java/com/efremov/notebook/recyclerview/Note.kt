package com.efremov.notebook.recyclerview

data class Note(val id: Int, var name: String, var content: String, var color: Int, var status: String = "active")