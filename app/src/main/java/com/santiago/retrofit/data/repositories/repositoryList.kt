package com.santiago.retrofit.data.repositories

import android.content.Context
import android.util.Log
import com.santiago.retrofit.data.local.ProductDatabase
import com.santiago.retrofit.data.local.ProductEntity
import com.santiago.retrofit.data.model.Product
import com.santiago.retrofit.data.model.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductRepository(private val context: Context) {
    private val database = ProductDatabase.getDatabase(context)
    private val productDao = database.productDao()

    private val predefinedProducts = listOf(
        Product(
            id = 1001,
            title = "Smartphone Galaxy Ultra",
            price = 999.99,
            description = "El último smartphone con cámara de 200MP y procesador de última generación. Incluye carga rápida de 45W y batería de 5000mAh.",
            category = "electronics",
            image = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500",
            rating = Rating(4.8, 156)
        ),
        Product(
            id = 1002,
            title = "Laptop Pro X",
            price = 1499.99,
            description = "Laptop profesional con pantalla OLED de 15.6 pulgadas, 32GB RAM y 1TB SSD. Perfecta para desarrollo y diseño.",
            category = "electronics",
            image = "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500",
            rating = Rating(4.9, 203)
        ),
        Product(
            id = 1003,
            title = "Auriculares Noise Cancelling",
            price = 299.99,
            description = "Auriculares inalámbricos con cancelación activa de ruido, 30 horas de batería y sonido Hi-Res.",
            category = "electronics",
            image = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500",
            rating = Rating(4.7, 189)
        ),
        Product(
            id = 1004,
            title = "Smartwatch Sport",
            price = 199.99,
            description = "Reloj inteligente con monitor cardíaco, GPS integrado y resistencia al agua. Ideal para deportistas.",
            category = "electronics",
            image = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500",
            rating = Rating(4.6, 142)
        ),
        Product(
            id = 1005,
            title = "Cámara Mirrorless Pro",
            price = 1299.99,
            description = "Cámara mirrorless profesional con sensor full-frame, 4K video y estabilización de imagen de 5 ejes.",
            category = "electronics",
            image = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500",
            rating = Rating(4.9, 178)
        )
    )

    val products: Flow<List<Product>> = productDao.getAllProducts().map { entities ->
        Log.d("ProductRepository", "Obteniendo datos desde Room Database")
        entities.map { it.toProduct() }
    }

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category).map { entities ->
            entities.map { it.toProduct() }
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toProduct() }
        }
    }

    suspend fun refreshProducts() {
        withContext(Dispatchers.IO) {
            try {
                Log.d("ProductRepository", "Obteniendo datos desde Retrofit")
                val products = RemoteConnection.service.getProducts()
                val entities = products.map { ProductEntity.fromProduct(it) }
                
                // Agregar los productos predefinidos
                val predefinedEntities = predefinedProducts.map { ProductEntity.fromProduct(it) }
                val allEntities = entities + predefinedEntities
                
                productDao.deleteAllProducts()
                productDao.insertProducts(allEntities)
                Log.d("ProductRepository", "Datos guardados en Room Database (${allEntities.size} productos)")
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error al obtener datos: ${e.message}")
                throw e
            }
        }
    }

    suspend fun getProductById(id: Int): Product? {
        return withContext(Dispatchers.IO) {
            val product = productDao.getProductById(id)?.toProduct()
            if (product != null) {
                Log.d("ProductRepository", "Producto $id obtenido desde Room Database")
            } else {
                Log.d("ProductRepository", "Producto $id no encontrado en Room Database")
            }
            product
        }
    }

    suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.updateProduct(ProductEntity.fromProduct(product))
            Log.d("ProductRepository", "Producto ${product.id} actualizado en Room Database")
        }
    }

    suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.deleteProduct(ProductEntity.fromProduct(product))
            Log.d("ProductRepository", "Producto ${product.id} eliminado de Room Database")
        }
    }

    suspend fun hasLocalData(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val count = productDao.getProductCount()
                val hasData = count > 0
                Log.d("ProductRepository", "Verificando datos locales: ${if (hasData) "Hay $count productos" else "No hay datos"}")
                hasData
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error al verificar datos locales: ${e.message}")
                false
            }
        }
    }
} 