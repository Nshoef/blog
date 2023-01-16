package com.example.blog.api.model

import com.example.blog.repository.entity.aRating
import java.time.Instant
import java.util.*

fun aPostRequest() = PostRequest(
    content = "Content of the post",
    author = "author")

fun aPostResponse() = PostResponse(
    id = UUID.randomUUID(),
    content = "Content of the post",
    author = "author",
    createdDate = Instant.now(),
    rating = aRating()
)
