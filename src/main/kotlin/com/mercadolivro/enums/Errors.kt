package com.mercadolivro.enums

enum class Errors(
    val code: String,
    var message: String
) {


    ML0O1( "ML-001","Invalid Request"  ),
    ML1O1( "ML-101","Book Id %s not exists"  ),
    ML1O2( "ML-102","Cannot update book with status %s"  ),
    ML201("ML-201","Customer Id %s not exists")
}