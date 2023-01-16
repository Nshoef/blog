package com.example.blog.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.Instant
import java.util.*


@Document(indexName = "blog")
class Post(
    @Id
    val id: UUID,
    @Field(type = FieldType.Text, name = "content")
    var content: String,
    @Field(type = FieldType.Text, name = "author")
    var author: String,
    @Field(type = FieldType.Date, name = "createdDate", format = [DateFormat.epoch_millis])
    var createdDate: Instant,
    @Field(type = FieldType.Nested, includeInParent = true)
    var rating: MutableMap<String, Boolean>,
) {


    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is Post -> false
            other.id != id || other.content != content || other.rating != rating
                    || other.author != author || other.createdDate != createdDate -> false
            else -> true
        }
    }

    override fun hashCode(): Int = this.toString().hashCode()

    override fun toString(): String {
        return "id=$id, content=$content, author=$author, createdDate=$createdDate, rating=$rating"
    }

}
