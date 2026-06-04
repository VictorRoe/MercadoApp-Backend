package dev.victorroe.mercadoapp.handler

import dev.victorroe.mercadoapp.exception.ResourceNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Resource not found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleInvalidEnumParam(ex: MethodArgumentTypeMismatchException): ResponseEntity<ProblemDetail> {
        val requiredType = ex.requiredType
        val supplied = ex.value?.toString() ?: "<empty>"
        val detail = if (requiredType != null && requiredType.isEnum) {
            val valid = requiredType.enumConstants?.joinToString(", ") ?: ""
            "Invalid value '$supplied' for parameter '${ex.name}'. Accepted values: $valid"
        } else {
            "Invalid value '$supplied' for parameter '${ex.name}'"
        }
        logger.warn(detail)
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ProblemDetail> {
        logger.error("Unhandled exception", ex)
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem)
    }
}
