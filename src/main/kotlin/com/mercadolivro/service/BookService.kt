package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    val repository: BookRepository
) {

    fun create(bookModel: BookModel) {
        repository.save(bookModel)
    }

    fun findAll(pageable: Pageable): Page<BookModel> {
        return repository.findAll(pageable);
    }

    fun findActives(pageable: Pageable): Page<BookModel> {
        return repository.findByStatus(BookStatus.ATIVO, pageable);
    }

    fun findById(id: Int): BookModel {
        return repository.findById(id).orElseThrow{NotFoundException(Errors.ML1O1.message.format(id), Errors.ML1O1.code)}
    }

    fun delete(id: Int){
        val book = this.findById(id)
        book.status = BookStatus.CANCELADO

        update(book)
    }

    fun update(book : BookModel) {
        if(!repository.existsById(book.id!!)){
            throw Exception("n existe pelo id")
        }

        repository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = repository.findByCustomer(customer)
        for(book in books){
            book.status = BookStatus.DELETADO
        }
        repository.saveAll(books)
    }

    fun findAllByIds(bookId: Set<Int>):List<BookModel> {
        return repository.findAllById(bookId).toList()
    }
}
