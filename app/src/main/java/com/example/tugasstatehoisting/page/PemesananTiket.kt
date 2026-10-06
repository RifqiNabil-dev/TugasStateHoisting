package com.example.tugasstatehoisting.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugasstatehoisting.ui.theme.TugasStateHoistingTheme
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

// Enum untuk tipe status tampilan box
enum class StatusType {
    NORMAL,
    LOADING,
    SUCCESS,
    ERROR
}


@Composable
fun TiketParentScreen(modifier: Modifier = Modifier) {
    var hargaTiket by remember { mutableIntStateOf(50000) }

    var jumlahTiket by remember { mutableIntStateOf(1) }

    var namaPembeli by remember { mutableStateOf("") }

    var isProcessing by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Silakan pesan tiket") }
    var statusType by remember { mutableStateOf(StatusType.NORMAL) }

    var orderTriggerKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(orderTriggerKey) {
        if (orderTriggerKey > 0) {
            if (namaPembeli.isBlank()) {

                isProcessing = false
                statusText = "Nama Masih Kosong"
                statusType = StatusType.ERROR
            } else {

                isProcessing = true
                statusText = "Memproses pesanan........."
                statusType = StatusType.LOADING

                delay(5000L)

                isProcessing = false
                statusText = "Tiket telah dipesan"
                statusType = StatusType.SUCCESS
            }
        }
    }

    TiketContent(
        modifier = modifier,
        namaPembeli = namaPembeli,
        onNamaChange = { newNama -> namaPembeli = newNama },
        hargaTiket = hargaTiket,
        jumlahTiket = jumlahTiket,
        onTambahJumlah = { jumlahTiket++ },
        onKurangJumlah = { if (jumlahTiket > 1) jumlahTiket-- },
        statusText = statusText,
        statusType = statusType,
        isProcessing = isProcessing,
        onPesanClick = {
            orderTriggerKey++
        }
    )
}


@Composable
fun TiketContent(
    modifier: Modifier = Modifier,
    namaPembeli: String,
    onNamaChange: (String) -> Unit,
    hargaTiket: Int,
    jumlahTiket: Int,
    onTambahJumlah: () -> Unit,
    onKurangJumlah: () -> Unit,
    statusText: String,
    statusType: StatusType,
    isProcessing: Boolean,
    onPesanClick: () -> Unit
) {
    val totalHarga = hargaTiket * jumlahTiket
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply {
        maximumFractionDigits = 0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Surface(
            color = Color(0xFF1976D2),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Pemesanan Tiket",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Nama",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = namaPembeli,
                            onValueChange = onNamaChange,
                            placeholder = { Text("Masukkan nama Anda") },
                            singleLine = true,
                            enabled = !isProcessing,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Harga Tiket",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${formatRupiah.format(hargaTiket)} / tiket",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = formatRupiah.format(totalHarga),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1976D2)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Jumlah Tiket",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onKurangJumlah,
                                enabled = jumlahTiket > 1 && !isProcessing,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("-", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            }

                            Text(
                                text = jumlahTiket.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Button(
                                onClick = onTambahJumlah,
                                enabled = !isProcessing,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("+", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = onPesanClick,
                        enabled = !isProcessing,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2),
                            disabledContainerColor = Color(0xFFB0BEC5)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Pesan Tiket",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            val (bgColor, textColor) = when (statusType) {
                StatusType.NORMAL -> Color(0xFFF0F0F0) to Color.DarkGray
                StatusType.LOADING -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
                StatusType.SUCCESS -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
                StatusType.ERROR -> Color(0xFFFFEBEE) to Color(0xFFC62828)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = bgColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (statusType) {
                        StatusType.LOADING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF1565C0)
                            )
                        }
                        StatusType.SUCCESS -> {
                            StatusBadge(
                                symbol = "✓",
                                badgeColor = Color(0xFF2E7D32)
                            )
                        }
                        StatusType.ERROR -> {
                            StatusBadge(
                                symbol = "!",
                                badgeColor = Color(0xFFC62828)
                            )
                        }
                        StatusType.NORMAL -> {
                            StatusBadge(
                                symbol = "i",
                                badgeColor = Color.DarkGray
                            )
                        }
                    }

                    Text(
                        text = "Status: $statusText",
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun StatusBadge(symbol: String, badgeColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(22.dp)
            .background(color = badgeColor, shape = CircleShape)
    ) {
        Text(
            text = symbol,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TiketParentScreenPreview() {
    TugasStateHoistingTheme {
        TiketParentScreen()
    }
}
