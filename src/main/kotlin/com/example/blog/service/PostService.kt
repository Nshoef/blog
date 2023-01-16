package com.example.blog.service

import com.example.blog.api.model.PostRequest
import com.example.blog.api.model.PostResponse
import com.example.blog.api.model.RatingRequest
import com.example.blog.exception.PostNotFoundException
import com.example.blog.mapping.PostMapper
import com.example.blog.repository.PostRepository
import com.example.blog.repository.entity.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostService @Autowired constructor(
    private val postRepository: PostRepository,
    private val postMapper: PostMapper
) {

    fun createPost(postRequest: PostRequest):PostResponse {
        return postMapper.postRequestToEntity(postRequest = postRequest)
            .let { postRepository.save(it) }
            .let { postMapper.postEntityToPostResponse(it) }
    }

    fun updatePost(id: UUID, postRequest: PostRequest):PostResponse {
        return postRepository.findById(id)
            .orElseThrow { PostNotFoundException("Post with id $id is not exist") }
            .let { postMapper.postRequestToEntity(id = id, createdDate = it.createdDate ,postRequest = postRequest) }
            .let { postRepository.save(it) }
            .let { postMapper.postEntityToPostResponse(it) }
    }

    fun getPost(id: UUID):PostResponse {
        return postRepository.findById(id)
            .orElseThrow { PostNotFoundException("Post with id $id is not exist") }
            .let { postMapper.postEntityToPostResponse(it) }
    }

    fun getPosts(string: String?, sortDirection: Direction?):List<PostResponse> {
        val sort = sortDirection?.let { Sort.by(it, Post::createdDate.name) }?: Sort.unsorted()
        return when(string) {
            null -> postRepository.findAll(sort)
            else -> postRepository.findAllByNameUsingAnnotations(string, sort)
        }
            .map { postMapper.postEntityToPostResponse(it) }
    }

    fun deletePost(id: UUID): PostResponse {
        return postRepository.findById(id)
            .orElseThrow { PostNotFoundException("Post with id $id is not exist") }
            .also { postRepository.deleteById(id) }
            .let { postMapper.postEntityToPostResponse(it) }
    }

    fun ratePost(id: UUID, ratingRequest: RatingRequest): PostResponse {
        return postRepository.findById(id)
            .orElseThrow { PostNotFoundException("Post with id $id is not exist") }
            .apply { this.rating[ratingRequest.user] = ratingRequest.like }
            .let { postRepository.save(it) }
            .let { postMapper.postEntityToPostResponse(it) }
    }

    fun unRatePost(id: UUID, user: String): PostResponse {
        return postRepository.findById(id)
            .orElseThrow { PostNotFoundException("Post with id $id is not exist") }
            .apply { this.rating.remove(user) }
            .let { postRepository.save(it) }
            .let { postMapper.postEntityToPostResponse(it) }
    }
}
