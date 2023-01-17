package com.example.blog.repository.entity

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

const val USER_1 = "user 1"
const val USER_2 = "user 2"
const val USER_3 = "user 3"
const val USER_4 = "user 4"
const val USER_5 = "user 5"

fun aPost(
    id: UUID = UUID.randomUUID(),
    content: String = "Content of the post is here",
    author: String = USER_1,
    createdDate: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS),
    rating: MutableMap<String, Boolean> = aRating()
) = Post(id, content, author, createdDate, rating)

fun aRating() = mutableMapOf(
    USER_2 to true,
    USER_3 to false,
    USER_4 to true,
    USER_5 to true
)
