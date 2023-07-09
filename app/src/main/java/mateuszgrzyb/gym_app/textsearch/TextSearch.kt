package mateuszgrzyb.gym_app.textsearch

import com.haroldadmin.lucilla.annotations.Id
import com.haroldadmin.lucilla.core.useFts

data class IndexedRow(
    @Id val id: Int,
    val sentence: String,
)

fun createIndex(rows: List<String>) = useFts(rows.mapIndexed { i, row -> IndexedRow(i, row) })
