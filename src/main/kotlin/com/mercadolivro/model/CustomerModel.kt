package com.mercadolivro.model

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Roles
import javax.persistence.*

@Entity(name="customer")
data class CustomerModel(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus,

    @Column
    val password: String,

    @CollectionTable(name="customer_roles", joinColumns = [JoinColumn ( name = "customer_id")])
    @ElementCollection(targetClass = Roles::class, fetch = FetchType.EAGER)
    @Column(name= "role")
    @Enumerated(EnumType.STRING)
    var role: Set<Roles>  = setOf()


)