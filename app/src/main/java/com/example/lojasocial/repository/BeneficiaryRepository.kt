package com.example.lojasocial.repository

import com.example.lojasocial.models.Beneficiary

object BeneficiaryRepository {

    private val beneficiaries = mutableListOf(
        Beneficiary(
            id = "1",
            name = "João Silva",
            studentNumber = "24334",
            course = "Engenharia de Sistemas Informáticos",
            active = true
        ),
        Beneficiary(
            id = "2",
            name = "Ana Costa",
            studentNumber = "24567",
            course = "Gestão",
            active = true
        ),
        Beneficiary(
            id = "3",
            name = "Guilherme Azeredo",
            studentNumber = "23510",
            course = "Engenharia de Sistemas Informáticos",
            active = true
        ),
        Beneficiary(
            id = "4",
            name = "Pedro Teixeira",
            studentNumber = "27427",
            course = "Engenharia Industrial",
            active = false
        ),
        Beneficiary(
            id = "5",
            name = "Tiago Augusto",
            studentNumber = "28123",
            course = "Fiscalidade",
            active = true
        ),
        Beneficiary(
            id = "6",
            name = "Rita Seabra",
            studentNumber = "20375",
            course = "Design Gráfico",
            active = true
        ),
        Beneficiary(
            id = "7",
            name = "Hugo Correia",
            studentNumber = "28259",
            course = "Gestão Hoteleira",
            active = false
        ),
        Beneficiary(
            id = "8",
            name = "Manuel Rodrigues",
            studentNumber = "21385",
            course = "Gestão de Empresas",
            active = true
        ),
        Beneficiary(
            id = "9",
            name = "Bruno Cunha",
            studentNumber = "29256",
            course = "Gestão de Empresas",
            active = false
        ),
        Beneficiary(
            id = "10",
            name = "Renato Figueiredo",
            studentNumber = "29865",
            course = "Gestão de Empresas",
            active = true
        ),
    )

    fun getAll(): List<Beneficiary> = beneficiaries

    fun getById(id: String): Beneficiary? =
        beneficiaries.firstOrNull { it.id == id }

    fun add(beneficiary: Beneficiary) {
        beneficiaries.add(
            beneficiary.copy(
                id = (beneficiaries.size + 1).toString()
            )
        )
    }

    fun update(updated: Beneficiary) {
        val index = beneficiaries.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            beneficiaries[index] = updated
        }
    }

    fun remove(id: String) {
        beneficiaries.removeAll { it.id == id }
    }
}
