package com.example.blog.repository

import com.example.blog.repository.entity.Post
import org.springframework.data.domain.Sort
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface PostRepository: ElasticsearchRepository<Post, UUID> {

   @Query("""{"match":{"content":"?0"}}""")
   fun findAllByNameUsingAnnotations(name: String, sort: Sort): List<Post>

}






