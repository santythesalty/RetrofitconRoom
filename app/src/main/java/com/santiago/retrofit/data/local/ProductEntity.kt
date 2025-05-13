package com.santiago.retrofit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.santiago.retrofit.data.model.Product
import com.santiago.retrofit.data.model.Rating

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val ratingRate: Double,
    val ratingCount: Int
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            description = description,
            category = category,
            image = image,
            rating = Rating(rate = ratingRate, count = ratingCount)
        )
    }

    companion object {
        fun fromProduct(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                title = product.title,
                price = product.price,
                description = product.description,
                category = product.category,
                image = product.image,
                ratingRate = product.rating.rate,
                ratingCount = product.rating.count
            )
        }
    }
} 