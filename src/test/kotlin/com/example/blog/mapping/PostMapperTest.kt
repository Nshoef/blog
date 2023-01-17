package com.example.blog.mapping

import com.example.blog.api.model.aPostRequest
import com.example.blog.repository.entity.aPost
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

class PostMapperTest {

    private val mapper = PostMapper()

    @Test
    fun `Should map post request to post entity`() {
        val id = UUID.randomUUID()
        val request = aPostRequest()
        val createdDate = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        val result = mapper.postRequestToEntity(id, createdDate, request)

        assertEquals(id, result.id)
        assertEquals(request.content, result.content)
        assertEquals(request.author, result.author)
        assertEquals(createdDate ,result.createdDate)
        assertTrue(result.rating.isEmpty())
    }

    @Test
    fun `Should map post entity to post response`() {
        val post = aPost()

        val result = mapper.postEntityToPostResponse(post)

        assertEquals(post.id, result.id)
        assertEquals(post.content, result.content)
        assertEquals(post.author, result.author)
        assertEquals(post.createdDate ,result.createdDate)
        assertEquals(post.rating ,result.rating)
    }
}
