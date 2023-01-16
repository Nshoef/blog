package com.example.blog.mapping

import com.example.blog.api.model.PostRequest
import com.example.blog.api.model.PostResponse
import com.example.blog.repository.entity.Post
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*


@Component
class PostMapper{

    fun postRequestToEntity(id: UUID = UUID.randomUUID(), createdDate: Instant = Instant.now(), postRequest: PostRequest) = Post(
        id = id,
        content = postRequest.content,
        author = postRequest.author,
        createdDate = createdDate,
        rating = mutableMapOf()
    )

    fun postEntityToPostResponse(post: Post) = PostResponse(
        id = post.id,
        content = post.content,
        author = post.author,
        createdDate = post.createdDate,
        rating = post.rating
    )
}
