package com.santiago.retrofit.ui.screen.DetailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.santiago.retrofit.data.model.Product

@Composable
fun DetailScreen(productId: Int, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(
            application = context.applicationContext as android.app.Application,
            productId = productId
        )
    )
    val uiState by viewModel.uiState.observeAsState(UiState())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F0FF) // Fondo suave morado claro
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Botón de retroceso
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                if (uiState.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF6B46C1) // Morado principal
                        )
                    }
                }

                uiState.error?.let { error ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: $error",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.Red
                            )
                        )
                    }
                }

                uiState.product?.let { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ProductImage(product = product)
                        ProductDetails(product = product)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductImage(product: Product) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = product.image,
                imageLoader = ImageLoader.Builder(context).crossfade(true).build()
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ProductDetails(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748) // Color de texto oscuro
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFECC94B), // Amarillo dorado
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = String.format("%.1f", product.rating.rate),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF718096) // Gris medio
                )
            )
            Text(
                text = "(${product.rating.count} reviews)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF718096) // Gris medio
                )
            )
        }

        Text(
            text = "$${product.price}",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B46C1) // Morado principal
            )
        )

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF4A5568) // Gris oscuro
            ),
            textAlign = TextAlign.Justify
        )

        Text(
            text = "Categoría: ${product.category}",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF718096) // Gris medio
            )
        )
    }
} 