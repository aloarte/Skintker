package com.p4r4d0x.data.repositories

import com.p4r4d0x.data.datasources.ResourcesDatasource
import com.p4r4d0x.domain.repository.ResourcesRepository

class ResourcesRepositoryImpl(private val resourcesDatasource: ResourcesDatasource) :
    ResourcesRepository {
    override fun getZonesReferenceMap(): Map<Int, Int> {
        return resourcesDatasource.getZonesReferenceMap()
    }

    override fun getFoodReferenceMap(): Map<Int, Int> {
       return resourcesDatasource.getFoodReferenceMap()
    }

    override fun getBeerTypesReferenceMap(): Map<Int, Int> {
        return resourcesDatasource.getBeerTypesReferenceMap()
    }
}