package com.example.blog.repository

import com.example.blog.repository.entity.Post
import com.example.blog.repository.entity.aPost
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import java.time.Instant
import java.time.temporal.ChronoUnit

class PostRepositoryTest: AbstractElasticsearchTest() {

    @Autowired
    private lateinit var postRepository: PostRepository


    private val post1 = aPost()
    private val post2 = aPost()
    private val post3 = aPost()


    @Before
    fun setup() {
        postRepository.saveAll(listOf(post1, post2, post3))
    }

    @After
    fun cleanup() {
       postRepository.deleteAll()
    }

    @Test
    fun `Should save posts`() {
        val post = aPost()

        val result = postRepository.save(post)

        assertTrue(postRepository.existsById(post.id))
        assertEquals(4, postRepository.findAll().count())
        assertEquals(post, result)
    }

    @Test
    fun `Should update posts`() {
        val update = aPost(id = post1.id)

        val result = postRepository.save(update)

        assertTrue(postRepository.existsById(post1.id))
        assertEquals(update, result)
    }

    @Test
    fun `Should get a post`() {
        val result1 = postRepository.findById(post1.id).get()
        val result2 = postRepository.findById(post3.id).get()

        assertEquals(post1, result1)
        assertEquals(post3, result2)
    }

    @Test
    fun `Should get all posts sorted ASC`() {
        val post1 = aPost(createdDate = Instant.now().minusSeconds(1000).truncatedTo(ChronoUnit.MILLIS))
        val post2 = aPost(createdDate = Instant.now().plusSeconds(1000).truncatedTo(ChronoUnit.MILLIS))
        val post3 = aPost(createdDate = Instant.now().truncatedTo(ChronoUnit.MILLIS))

        postRepository.saveAll(listOf(post1, post2, post3))

        val result = postRepository.findAll(Sort.by(Sort.Direction.ASC, Post::createdDate.name))

        assertEquals(6, result.count())
        assertEquals(post1, result.first())
        assertEquals(post2, result.last())
    }

    @Test
    fun `Should get all posts by matching string`() {
        val post1 = aPost(content = "Unmatched")
        val post2 = aPost(content = "Searching for this")
        val post3 = aPost(content = "or maybe this?")

        postRepository.saveAll(listOf(post1, post2, post3))

        val result = postRepository.findAllByNameUsingAnnotations("this", Sort.by(Sort.Direction.DESC, Post::createdDate.name))

        assertEquals(2, result.count())
        assertEquals(post2, result.first())
        assertEquals(post3, result.last())
    }

    @Test
    fun `Should delete posts`() {
        postRepository.deleteById(post2.id)

        assertFalse(postRepository.existsById(post2.id))
        assertEquals(2, postRepository.findAll().count())
    }
}
