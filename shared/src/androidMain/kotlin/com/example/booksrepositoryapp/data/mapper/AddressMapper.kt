package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.remote.firebase.firestore.AddressModelFB
import com.example.booksrepositoryapp.domain.model.Address

fun AddressModelFB.toDomain(): Address {
    return Address(
        id = id,
        house = house,
        street = street,
        area = area,
        city = city,
        postalCode = postalCode,
        country = country,
        fullAddress = fullAddress,
        latitude = latitude,
        longitude = longitude,
        isSelected = isSelected,
        createdAt = createdAt
    )
}

fun Address.toFirestore(): AddressModelFB {
    return AddressModelFB(
        id = id,
        house = house,
        street = street,
        area = area,
        city = city,
        postalCode = postalCode,
        country = country,
        fullAddress = fullAddress,
        latitude = latitude,
        longitude = longitude,
        isSelected = isSelected,
        createdAt = createdAt
    )
}
