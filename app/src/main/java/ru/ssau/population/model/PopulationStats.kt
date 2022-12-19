package ru.ssau.ais.model

interface PopulationStats {
    /**
     * Фактор самовоспроизводства в отсутствие столкновений с другими популяциями.
     * Например, травоядные будут иметь положительный коэффициент, т.к. они размножаются за счёт природных ресурсов, а не за счёт убийства других животных.
     * А вот хищники будут иметь отрицательный коэффициент, т.к. они голодают в отсутствие добычи.
     */
    val selfReproductionFactor: Double

    /**
     * Фактор атаки на другие популяции.
     * Например, травоядные будут иметь нулевой коэффициент, т.к. они не атакуют другие популяции.
     * Хищник же будет иметь положительный коэффициент, т.к. он атакует остальные популяции.
     */
    val attackFactor: Double

    /**
     * Фактор защиты от атак других популяций.
     * Уменьшает урон, получаемый от атак других популяций, умножая его на этот коэффициент.
     */
    val defenseFactor: Double

    /**
     * Питательность особи данной популяции для атакующих популяций.
     */
    val nutrition: Double

    /**
     * Фактор голода хищников.
     * Характеризует прирост хищной популяции после атаки на популяцию жертвы, в зависимости от питательности популяции жертвы.
     * У травоядных он не играет роли, т.к. они не размножатся за счёт атаки.
     */
    val hungerFactor: Double
}
