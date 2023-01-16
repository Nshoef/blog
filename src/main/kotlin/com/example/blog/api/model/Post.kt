package com.example.blog.api.model

import java.time.Instant
import java.util.UUID

data class PostRequest(
    val content: String,
    val author: String
)

data class PostResponse(
    val id: UUID,
    val content: String,
    val author: String,
    val createdDate: Instant,
    val rating: Map<String, Boolean>
)

