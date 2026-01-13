package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Alert
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import kotlin.math.abs


object AlertRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val ALERTS = "alerts"
    private const val PRODUCTS = "products"
    private const val DELIVERIES = "deliveries"

    /* ===================== OBTENÇÃO ===================== */

    suspend fun getActive(): List<Alert> {
        return db.collection(ALERTS)
            .whereEqualTo("resolved", false)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Alert::class.java)?.copy(id = doc.id)
            }
    }

    /* ===================== RESOLVER ===================== */

    suspend fun resolve(alertId: String) {
        db.collection(ALERTS)
            .document(alertId)
            .update(
                mapOf(
                    "resolved" to true,
                    "resolvedAt" to Timestamp.now()
                )
            )
            .await()
    }

    /* ===================== GERAÇÃO GERAL ===================== */

    suspend fun generateAllAlerts(
        lowStockThreshold: Int = 5,
        expiringSoonDays: Int = 7,
        deliveryOverdueDays: Int = 2
    ): ResultWrapper<Int> {
        return try {
            var created = 0
            created += generateFromProducts(lowStockThreshold, expiringSoonDays)
            created += generateFromDeliveries(deliveryOverdueDays)
            created += checkOverdueDeliveries(deliveryOverdueDays)
            ResultWrapper.Success(created)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    /* ===================== PRODUTOS ===================== */

    private suspend fun generateFromProducts(
        lowStockThreshold: Int,
        expiringSoonDays: Int
    ): Int {
        val now = System.currentTimeMillis()
        val dayMs = 1000L * 60 * 60 * 24

        val productsSnap = db.collection(PRODUCTS).get().await()
        var created = 0

        for (p in productsSnap.documents) {
            val productId = p.id
            val name = p.getString("name") ?: "Produto"
            val quantity = p.getLong("quantity")?.toInt() ?: 0
            val expireDateMs = p.getLong("expireDate")

            // Produto esgotado
            if (quantity == 0) {
                created += createIfNotExists(
                    type = "Produto esgotado",
                    entityId = productId,
                    title = "Produto esgotado",
                    message = "O produto $name encontra-se esgotado.",
                    severity = "CRITICO"
                )
            }
            // Stock baixo
            else if (quantity < lowStockThreshold) {
                created += createIfNotExists(
                    type = "Stock baixo",
                    entityId = productId,
                    title = "Stock baixo",
                    message = "O produto $name está com stock baixo (quantidade atual: $quantity).",
                    severity = "AVISO"
                )
            }

            // Validade
            if (expireDateMs != null) {
                if (expireDateMs < now) {
                    created += createIfNotExists(
                        type = "EXPIRADO",
                        entityId = productId,
                        title = "Produto fora de validade",
                        message = "O produto $name encontra-se fora de validade.",
                        severity = "CRITICO"
                    )
                } else {
                    val daysUntil = ((expireDateMs - now) / dayMs).toInt()
                    if (daysUntil in 0..expiringSoonDays) {
                        created += createIfNotExists(
                            type = "Validade a terminar",
                            entityId = productId,
                            title = "Validade a terminar",
                            message = "O produto $name expira dentro de $daysUntil dia(s).",
                            severity = "PERIGO"
                        )
                    }
                }
            }
        }
        return created
    }

    /* ===================== ENTREGAS ===================== */

    private suspend fun generateFromDeliveries(
        deliveryOverdueDays: Int
    ): Int {
        val now = System.currentTimeMillis()
        val dayMs = 1000L * 60 * 60 * 24

        val deliveriesSnap = db.collection(DELIVERIES).get().await()
        val productsSnap = db.collection(PRODUCTS).get().await()

        val stockByProductId = productsSnap.documents.associate {
            it.id to (it.getLong("quantity")?.toInt() ?: 0)
        }

        var created = 0

        for (d in deliveriesSnap.documents) {
            val data = d.data ?: continue

            val deliveryId = d.id
            val beneficiary = data["beneficiaryName"] as? String ?: ""
            val state = data["state"] as? Boolean ?: false
            val dateStr = data["date"] as? String ?: ""
            val items = data["items"] as? List<*>

            // Entrega pendente
            if (!state) {
                val dateMs = parseDateToMillis(dateStr)
                if (dateMs != null) {
                    val days = ((now - dateMs) / dayMs).toInt()
                    if (days >= deliveryOverdueDays) {
                        created += createIfNotExists(
                            type = "Entrega pendente",
                            entityId = deliveryId,
                            title = "Entrega pendente",
                            message = "A entrega ao beneficiário $beneficiary encontra-se pendente há $days dia(s).",
                            severity = "PERIGO"
                        )
                    }
                }
            }

            // Entrega sem produtos
            if (items.isNullOrEmpty()) {
                created += createIfNotExists(
                    type = "Entrega sem produtos",
                    entityId = deliveryId,
                    title = "Entrega sem produtos",
                    message = "A entrega ao beneficiário $beneficiary não tem produtos associados.",
                    severity = "PERIGO"
                )
                continue
            }

            /**
            // Stock insuficiente na entrega
            val itemsList = items.mapNotNull { it as? Map<*, *> }
            for (item in itemsList) {
                val productId = item["id"] as? String ?: continue
                val productName = item["name"] as? String ?: "Produto"
                val requested = (item["quantity"] as? Long)?.toInt() ?: 0
                val stock = stockByProductId[productId] ?: 0

                if (requested > stock) {
                    created += createIfNotExists(
                        type = "Stock insuficiente na entrega",
                        entityId = deliveryId,
                        title = "Stock insuficiente na entrega",
                        message = "Na entrega ao beneficiário $beneficiary, o produto $productName tem quantidade solicitada superior ao stock disponível.",
                        severity = "CRITICO"
                    )
                }
            }**/

            // Entrega sem beneficiário
            if (beneficiary.isBlank()) {
                created += createIfNotExists(
                    type = "Beneficiário em falta",
                    entityId = deliveryId,
                    title = "Beneficiário em falta",
                    message = "Existe uma entrega registada sem beneficiário associado.",
                    severity = "PERIGO"
                )
            }
        }

        return created
    }


    suspend fun checkOverdueDeliveries(
        overdueDays: Int = 2
    ): Int {
        var created = 0

        val deliveriesSnap = db.collection(DELIVERIES).get().await()

        for (d in deliveriesSnap.documents) {
            val data = d.data ?: continue

            val deliveryId = d.id
            val beneficiary = data["beneficiaryName"] as? String ?: ""
            val isDelivered = data["state"] as? Boolean ?: false
            val dateStr = data["date"] as? String ?: ""

            // Ignore completed deliveries
            if (isDelivered) continue

            val deliveryDateMs = parseDateToMillis(dateStr) ?: continue

            // Truncate today's date to 00:00:00
            val todayMs = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val dayMs = 1000L * 60 * 60 * 24
            val daysDiff = ((todayMs - deliveryDateMs) / dayMs).toInt()

            when {
                // LATE
                daysDiff > 0 -> {
                    val messageText = if (daysDiff == 1) "$daysDiff dia" else "$daysDiff dias"
                    created += createIfNotExists(
                        type = "Entrega atrasada",
                        entityId = deliveryId,
                        title = "Entrega atrasada",
                        message = "A entrega ao beneficiário $beneficiary encontra-se atrasada por $messageText.",
                        severity = "CRÍTICO"
                    )
                }

                // TODAY
                daysDiff == 0 -> {
                    created += createIfNotExists(
                        type = "Entrega Hoje",
                        entityId = deliveryId,
                        title = "Entrega Hoje",
                        message = "A entrega ao beneficiário $beneficiary é hoje.",
                        severity = "AVISO"
                    )
                }

                // 1 or 2 days away
                daysDiff in -2..-1 -> {
                    val days = abs(daysDiff) // convert to positive
                    val messageText = if (days == 1) "$days dia" else "$days dias"
                    created += createIfNotExists(
                        type = "Entrega em $days dias",
                        entityId = deliveryId,
                        title = "Entrega em $days dia(s)",
                        message = "A entrega ao beneficiário $beneficiary está marcada para daqui a $messageText.",
                        severity = "AVISO"
                    )
                }
            }
        }

        return created
    }




    /* ===================== HELPERS ===================== */

    private suspend fun createIfNotExists(
        type: String,
        entityId: String,
        title: String,
        message: String,
        severity: String
    ): Int {
        val existing = db.collection(ALERTS)
            .whereEqualTo("resolved", false)
            .whereEqualTo("type", type)
            .whereEqualTo("entityId", entityId)
            .limit(1)
            .get()
            .await()

        if (!existing.isEmpty) return 0

        val alert = hashMapOf(
            "type" to type,
            "entityId" to entityId,
            "title" to title,
            "message" to message,
            "severity" to severity,
            "createdAt" to Timestamp.now(),
            "resolved" to false
        )

        db.collection(ALERTS).add(alert).await()
        return 1
    }

    private fun parseDateToMillis(dateStr: String): Long? {
        if (dateStr.isBlank()) return null

        // dd/MM/yyyy
        runCatching {
            val parts = dateStr.split("/")
            if (parts.size == 3) {
                val cal = Calendar.getInstance().apply {
                    set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt(), 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                return cal.timeInMillis
            }
        }

        // yyyy-MM-dd
        runCatching {
            val parts = dateStr.split("-")
            if (parts.size == 3) {
                val cal = Calendar.getInstance().apply {
                    set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt(), 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                return cal.timeInMillis
            }
        }

        return null
    }
}
