package com.example.blog.service

import com.example.blog.api.model.RatingRequest
import com.example.blog.api.model.aPostRequest
import com.example.blog.api.model.aPostResponse
import com.example.blog.exception.PostNotFoundException
import com.example.blog.mapping.PostMapper
import com.example.blog.repository.PostRepository
import com.example.blog.repository.entity.USER_1
import com.example.blog.repository.entity.aPost
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import org.springframework.data.domain.Sort
import java.util.*


class PostServiceTest {

    private val postRepository: PostRepository = mock()
    private val postMapper: PostMapper = mock()
    private val postService = PostService(postRepository, postMapper)


    @Test
    fun `Should create a new post from request`() {
        val postRequest = aPostRequest()
        val post = aPost(rating = mutableMapOf())
        val postResponse = aPostResponse()

        whenever(postMapper.postRequestToEntity(any(), any(), postRequest = eq(postRequest))).thenReturn(post)
        whenever(postRepository.save(post)).thenReturn(post)
        whenever(postMapper.postEntityToPostResponse(post)).thenReturn(postResponse)

        val result = postService.createPost(postRequest)

        assertEquals(postResponse, result)
    }

    @Test
    fun `Should update an existing post based on request`() {
        val postRequest = aPostRequest()
        val post = aPost(rating = mutableMapOf())
        val postResponse = aPostResponse()

        whenever(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        whenever(postMapper.postRequestToEntity(post.id, post.createdDate, postRequest)).thenReturn(post)
        whenever(postRepository.save(post)).thenReturn(post)
        whenever(postMapper.postEntityToPostResponse(post)).thenReturn(postResponse)

        val result = postService.updatePost(post.id, postRequest)

        assertEquals(postResponse, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun `Should throw exception when try to update non existing post`() {
        val postRequest = aPostRequest()
        val postId = UUID.randomUUID()

        whenever(postRepository.findById(postId)).thenReturn(Optional.empty())
        postService.updatePost(postId, postRequest)
    }

    @Test(expected = PostNotFoundException::class)
    fun `Should throw exception when requested post not exist`() {
       val postId = UUID.randomUUID()

        whenever(postRepository.findById(postId)).thenReturn(Optional.empty())
        postService.getPost(postId)
    }

    @Test
    fun `Should get requested post`() {
        val post = aPost(rating = mutableMapOf())
        val postResponse = aPostResponse()

        whenever(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        whenever(postMapper.postEntityToPostResponse(post)).thenReturn(postResponse)

        val result = postService.getPost(post.id)

        assertEquals(postResponse, result)
    }

    @Test
    fun `Should get requested posts`() {
        val post1 = aPost()
        val post2 = aPost()
        val post3 = aPost()
        val post4 = aPost()
        val post5 = aPost()

        val postResponse1 = aPostResponse()
        val postResponse2 = aPostResponse()
        val postResponse3 = aPostResponse()
        val postResponse4 = aPostResponse()
        val postResponse5 = aPostResponse()

        whenever(postRepository.findAll(Sort.unsorted())).thenReturn(listOf(post1, post2, post3, post4, post5))
        whenever(postMapper.postEntityToPostResponse(post1)).thenReturn(postResponse1)
        whenever(postMapper.postEntityToPostResponse(post2)).thenReturn(postResponse2)
        whenever(postMapper.postEntityToPostResponse(post3)).thenReturn(postResponse3)
        whenever(postMapper.postEntityToPostResponse(post4)).thenReturn(postResponse4)
        whenever(postMapper.postEntityToPostResponse(post5)).thenReturn(postResponse5)

        val result = postService.getPosts(null , null)

        assertEquals(listOf(postResponse1, postResponse2, postResponse3, postResponse4, postResponse5), result)
    }

    @Test
    fun `Should get requested posts with given sorting`() {
        val post1 = aPost()
        val post2 = aPost()
        val post3 = aPost()
        val post4 = aPost()
        val post5 = aPost()

        val postResponse1 = aPostResponse()
        val postResponse2 = aPostResponse()
        val postResponse3 = aPostResponse()
        val postResponse4 = aPostResponse()
        val postResponse5 = aPostResponse()

        whenever(postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"))).thenReturn(listOf(post1, post2, post3, post4, post5))
        whenever(postMapper.postEntityToPostResponse(post1)).thenReturn(postResponse1)
        whenever(postMapper.postEntityToPostResponse(post2)).thenReturn(postResponse2)
        whenever(postMapper.postEntityToPostResponse(post3)).thenReturn(postResponse3)
        whenever(postMapper.postEntityToPostResponse(post4)).thenReturn(postResponse4)
        whenever(postMapper.postEntityToPostResponse(post5)).thenReturn(postResponse5)

        val result = postService.getPosts(null , Sort.Direction.DESC)

        assertEquals(listOf(postResponse1, postResponse2, postResponse3, postResponse4, postResponse5), result)
    }

    @Test
    fun `Should get requested posts with given search string`() {
        val post1 = aPost()
        val post2 = aPost()
        val post3 = aPost()
        val post4 = aPost()
        val post5 = aPost()

        val postResponse1 = aPostResponse()
        val postResponse2 = aPostResponse()
        val postResponse3 = aPostResponse()
        val postResponse4 = aPostResponse()
        val postResponse5 = aPostResponse()

        whenever(postRepository.findAllByNameUsingAnnotations("text", Sort.unsorted())).thenReturn(listOf(post1, post2, post3, post4, post5))
        whenever(postMapper.postEntityToPostResponse(post1)).thenReturn(postResponse1)
        whenever(postMapper.postEntityToPostResponse(post2)).thenReturn(postResponse2)
        whenever(postMapper.postEntityToPostResponse(post3)).thenReturn(postResponse3)
        whenever(postMapper.postEntityToPostResponse(post4)).thenReturn(postResponse4)
        whenever(postMapper.postEntityToPostResponse(post5)).thenReturn(postResponse5)

        val result = postService.getPosts("text" , null)

        assertEquals(listOf(postResponse1, postResponse2, postResponse3, postResponse4, postResponse5), result)
    }

    @Test
    fun `Should delete requested post`() {
        val post = aPost()
        val postResponse = aPostResponse()

        whenever(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        doNothing().whenever(postRepository).deleteById(post.id)
        whenever(postMapper.postEntityToPostResponse(post)).thenReturn(postResponse)

        val result = postService.deletePost(post.id)

        assertEquals(postResponse, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun `Should throw exception when trying to delete none existing post`() {
        val postId = UUID.randomUUID()

        whenever(postRepository.findById(postId)).thenReturn(Optional.empty())
        postService.deletePost(postId)
    }

    @Test
    fun `Should add rating to post`() {
        val post = aPost()
        val ratingRequest = RatingRequest(true, "new user")
        val postResponse = aPostResponse()

        whenever(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        whenever(postRepository.save(post.apply { this.rating[ratingRequest.user] = ratingRequest.like })).thenReturn(post)
        whenever(postMapper.postEntityToPostResponse(post)).thenReturn(postResponse)

        val result = postService.ratePost(post.id, ratingRequest)

        assertEquals(postResponse, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun `Should throw exception when trying to rate none existing post`() {
        val postId = UUID.randomUUID()
        val ratingRequest = RatingRequest(true, "new user")

        whenever(postRepository.findById(postId)).thenReturn(Optional.empty())
        postService.ratePost(postId, ratingRequest)
    }

    @Test
    fun `Should remove rating from post`() {
        val post = aPost()
        val postResponse = aPostResponse()

        whenever(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        whenever(postRepository.save(post.apply { this.rating.remove(USER_1) })).thenReturn(post)
        whenever(postMapper.postEntityToPostResponse(post)).thenReturn(postResponse)

        val result = postService.unRatePost(post.id, USER_1)

        assertEquals(postResponse, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun `Should throw exception when trying to remove rate none existing post`() {
        val postId = UUID.randomUUID()

        whenever(postRepository.findById(postId)).thenReturn(Optional.empty())
        postService.unRatePost(postId, USER_1)
    }
}
