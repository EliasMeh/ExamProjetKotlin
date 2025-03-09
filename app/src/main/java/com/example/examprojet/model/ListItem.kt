package com.example.examprojet.model

data class ListItem(
    val imageSrc: Int, // Resource ID for the image
    val name: String,
    val description: String,
    val destination: String // Navigation destination
)