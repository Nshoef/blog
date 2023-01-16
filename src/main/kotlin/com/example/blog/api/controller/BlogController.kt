package com.example.blog.api.controller

import com.example.blog.api.model.PostRequest
import com.example.blog.api.model.PostResponse
import com.example.blog.api.model.RatingRequest
import com.example.blog.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
class BlogController @Autowired constructor(
    private val postService: PostService
) {

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(@RequestBody postRequest: PostRequest): PostResponse = postService.createPost(postRequest)

    @PutMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePost(@PathVariable id: UUID,  @RequestBody postRequest: PostRequest): PostResponse = postService.updatePost(id, postRequest)

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPost(@PathVariable id: UUID): PostResponse = postService.getPost(id)

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    fun getPosts(@RequestParam sort: Sort.Direction?, @RequestParam string: String?): List<PostResponse> = postService.getPosts(string, sort)

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deletePost(@PathVariable id: UUID): PostResponse = postService.deletePost(id)

    @PutMapping("/posts/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    fun ratePost(@PathVariable id: UUID, @RequestBody ratingRequest: RatingRequest) = postService.ratePost(id, ratingRequest)

    @DeleteMapping("/posts/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    fun unRatePost(@PathVariable id: UUID, @RequestParam user: String) = postService.unRatePost(id, user)
}
