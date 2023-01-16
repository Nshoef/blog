package com.example.blog.api.controller

import com.example.blog.api.model.RatingRequest
import com.example.blog.api.model.aPostRequest
import com.example.blog.api.model.aPostResponse
import com.example.blog.configuration.SecurityConfiguration
import com.example.blog.exception.PostNotFoundException
import com.example.blog.repository.entity.Post
import com.example.blog.service.PostService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@RunWith(SpringRunner::class)
@Import(SecurityConfiguration::class)
@WebMvcTest(controllers = [BlogController::class])
class BlogControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var postService: PostService

    private val objectMapper = ObjectMapper().also { it.findAndRegisterModules() }


    @Test
    fun `Should return 401 when trying to send a request without valid user`() {
        val request = aPostRequest()
        val expected = aPostResponse().copy(rating = emptyMap())

        whenever(postService.createPost(request)).thenReturn(expected)

        mockMvc.perform(
            post("/posts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(aPostRequest()))
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            put("/posts/${expected.id}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(aPostRequest()))
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            get("/posts/${expected.id}")
                .contentType("application/json")
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            get("/posts")
                .contentType("application/json")
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            delete("/posts/${expected.id}")
                .contentType("application/json")
        )
            .andExpect(status().isUnauthorized)


    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to create a new post`() {
        val request = aPostRequest()
        val expected = aPostResponse().copy(rating = emptyMap())

        whenever(postService.createPost(request)).thenReturn(expected)

        val response = mockMvc.perform(
            post("/posts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(aPostRequest()))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, Post::class.java)

        assertEquals(expected.id, result.id)
        assertEquals(expected.author, result.author)
        assertEquals(expected.content, result.content)
        assertEquals(expected.createdDate, result.createdDate)
        assertEquals(expected.rating, result.rating)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to update a post`() {
        val request = aPostRequest()
        val expected = aPostResponse().copy(rating = emptyMap())

        whenever(postService.updatePost(expected.id, request)).thenReturn(expected)

        val response = mockMvc.perform(
            put("/posts/${expected.id}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(aPostRequest()))
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, Post::class.java)

        assertEquals(expected.id, result.id)
        assertEquals(expected.author, result.author)
        assertEquals(expected.content, result.content)
        assertEquals(expected.createdDate, result.createdDate)
        assertEquals(expected.rating, result.rating)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to get a post by id`() {
        val expected = aPostResponse()

        whenever(postService.getPost(expected.id)).thenReturn(expected)

        val response = mockMvc.perform(
            get("/posts/${expected.id}")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, Post::class.java)

        assertEquals(expected.id, result.id)
        assertEquals(expected.author, result.author)
        assertEquals(expected.content, result.content)
        assertEquals(expected.createdDate, result.createdDate)
        assertEquals(expected.rating, result.rating)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to get a posts list`() {
        val post1 = aPostResponse().copy(rating = emptyMap())
        val post2 = aPostResponse()
        val post3 = aPostResponse()

        val expected = listOf(post1, post2, post3)

        whenever(postService.getPosts(null , null)).thenReturn(expected)

        val response = mockMvc.perform(
            get("/posts")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, object : TypeReference<List<Post>>() {})

        assertEquals(result.size, expected.size)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to get a posts list with sorting direction`() {
        val post1 = aPostResponse().copy(rating = emptyMap())
        val post2 = aPostResponse()
        val post3 = aPostResponse()

        val expected = listOf(post1, post2, post3)

        whenever(postService.getPosts(null , Sort.Direction.DESC)).thenReturn(expected)

        val response = mockMvc.perform(
            get("/posts")
                .param("sort", "DESC")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, object : TypeReference<List<Post>>() {})

        assertEquals(result.size, expected.size)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to get a posts list with text search param`() {
        val post1 = aPostResponse().copy(rating = emptyMap())
        val post2 = aPostResponse()
        val post3 = aPostResponse()

        val expected = listOf(post1, post2, post3)

        whenever(postService.getPosts("bla" , null)).thenReturn(expected)

        val response = mockMvc.perform(
            get("/posts")
                .param("string", "bla")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, object : TypeReference<List<Post>>() {})

        assertEquals(result.size, expected.size)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to get posts list with text search param and sorting direction`() {
        val post1 = aPostResponse().copy(rating = emptyMap())
        val post2 = aPostResponse()
        val post3 = aPostResponse()

        val expected = listOf(post1, post2, post3)

        whenever(postService.getPosts("bla" , Sort.Direction.ASC)).thenReturn(expected)

        val response = mockMvc.perform(
            get("/posts")
                .param("string", "bla")
                .param("sort", "ASC")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, object : TypeReference<List<Post>>() {})

        assertEquals(result.size, expected.size)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to delete a posts`() {
        val expected = aPostResponse()

        whenever(postService.deletePost(expected.id)).thenReturn(expected)

        val response = mockMvc.perform(
            delete("/posts/${expected.id}")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, Post::class.java)

        assertEquals(expected.id, result.id)
        assertEquals(expected.author, result.author)
        assertEquals(expected.content, result.content)
        assertEquals(expected.createdDate, result.createdDate)
        assertEquals(expected.rating, result.rating)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to rate a posts`() {
        val rateRequest = RatingRequest(true, "other.user")
        val expected = aPostResponse()

        whenever(postService.ratePost(expected.id, rateRequest)).thenReturn(expected)

        val response = mockMvc.perform(
            put("/posts/${expected.id}/rating")
                .content(objectMapper.writeValueAsString(rateRequest))
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, Post::class.java)

        assertEquals(expected.id, result.id)
        assertEquals(expected.author, result.author)
        assertEquals(expected.content, result.content)
        assertEquals(expected.createdDate, result.createdDate)
        assertEquals(expected.rating, result.rating)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should except request to remove a rate from a posts`() {

        val expected = aPostResponse()

        whenever(postService.unRatePost(expected.id, expected.rating.keys.first())).thenReturn(expected)

        val response = mockMvc.perform(
            delete("/posts/${expected.id}/rating")
                .param("user", expected.rating.keys.first())
        )
            .andExpect(status().isOk)
            .andReturn()

        val result = objectMapper.readValue(response.response.contentAsString, Post::class.java)

        assertEquals(expected.id, result.id)
        assertEquals(expected.author, result.author)
        assertEquals(expected.content, result.content)
        assertEquals(expected.createdDate, result.createdDate)
        assertEquals(expected.rating, result.rating)
    }

    @Test
    @WithUserDetails("admin")
    fun `Should return 404 when PostNotFoundException is thrown`() {
        val id = UUID.randomUUID()
        val user = "user"
        val message = "Can not find post with id $id"

        whenever(postService.unRatePost(id, user)).thenThrow(PostNotFoundException(message))

        val response = mockMvc.perform(
            delete("/posts/${id}/rating")
                .param("user", user)
        )
            .andExpect(status().isNotFound)
            .andReturn()

        assertEquals(message, response.response.contentAsString)
    }
}
