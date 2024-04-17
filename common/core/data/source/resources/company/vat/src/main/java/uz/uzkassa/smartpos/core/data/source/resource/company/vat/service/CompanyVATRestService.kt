package uz.uzkassa.smartpos.core.data.source.resource.company.vat.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVATResponse

interface CompanyVATRestService {

    fun getCompanyVAT(): Flow<List<CompanyVATResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): CompanyVATRestService =
            CompanyVATRestServiceImpl(retrofit.create())
    }
}