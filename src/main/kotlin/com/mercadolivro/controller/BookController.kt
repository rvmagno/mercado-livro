package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.PageResponse
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toBookResponse
import com.mercadolivro.extension.toPageResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("books")
class BookController(
    private val service: BookService,
    private val customerService: CustomerService
) {


    @PostMapping
    fun create(@RequestBody @Valid request : PostBookRequest){
        val customer = customerService.findById(request.customerId)
        service.create(request.toBookModel(customer))
    }

    @GetMapping
    fun findAll(@PageableDefault(page = 0, size = 10) pageable : Pageable): PageResponse<BookResponse> {
        return service.findAll(pageable).map{ it.toBookResponse() }.toPageResponse()
    }

    @GetMapping("/ativos")
    fun findActives(@PageableDefault(page = 0, size = 10) pageable : Pageable): Page<BookResponse>{
        return service.findActives(pageable).map { it.toBookResponse() };
    }

    @GetMapping("{id}")
    fun findById(@PathVariable("id") id: Int) : BookResponse = service.findById(id).toBookResponse()


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Int) {
        service.delete(id)
    }


    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable("id") id: Int, @RequestBody book : PutBookRequest) {
        val model = service.findById(id)
        service.update(book.toBookModel(model))
    }
}