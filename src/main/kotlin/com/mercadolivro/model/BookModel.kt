package com.mercadolivro.model

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.BadRequestException
import java.math.BigDecimal
import javax.persistence.*

@Entity(name="book")
data class BookModel(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne @JoinColumn(name="customer_id")
    var customer: CustomerModel? = null
){


    @Column @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
    set(value) {
        if(field == BookStatus.CANCELADO || field == BookStatus.DELETADO){
            throw BadRequestException(Errors.ML1O2.message.format(field.toString()), Errors.ML1O2.code)
        }
        field = value
    }

    constructor(
        id: Int? = null,
        name: String,
        price: BigDecimal,
        status: BookStatus?,
        customer: CustomerModel?) : this(id, name, price, customer){
            this.status = status
        }

}
