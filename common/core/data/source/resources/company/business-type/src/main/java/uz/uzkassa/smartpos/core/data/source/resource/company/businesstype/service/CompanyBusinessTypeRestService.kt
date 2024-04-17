package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse

interface CompanyBusinessTypeRestService {

    fun getCompanyBusinessTypes(): Flow<List<CompanyBusinessTypeResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): CompanyBusinessTypeRestService =
            CompanyBusinessTypeRestServiceImpl(retrofit.create())
    }
}