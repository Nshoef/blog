package com.example.blog.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFoundException(ex: PostNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(ex.message, HttpHeaders(), HttpStatus.NOT_FOUND)
    }
}
