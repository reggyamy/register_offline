package com.reggya.registeroffline.data

import android.content.Context
import android.util.Log
import com.reggya.registeroffline.data.local.MemberLocalDataSource
import com.reggya.registeroffline.data.remote.network.ApiService
import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.data.utils.mapper.toDomain
import com.reggya.registeroffline.data.utils.mapper.toDomainFromResponse
import com.reggya.registeroffline.data.utils.mapper.toEntity
import com.reggya.registeroffline.data.utils.mapper.toMultipartMap
import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.domain.repositories.MemberRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import javax.inject.Singleton
import com.reggya.registeroffline.data.utils.uriparse.UriParse

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: MemberLocalDataSource,
    @ApplicationContext private val context: Context
) : MemberRepository {

    private val uriParse by lazy { UriParse(context) }

    override suspend fun registerMember(member: Member) {
        localDataSource.insertMember(member.toEntity())
    }

    override fun getMemberList(): Flow<Result<List<Member>>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.getListMember()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (!body.isNullOrEmpty()) {
                        emit(Result.Success(body.toDomainFromResponse()))
                    } else {
                        emit(Result.Error(message = "Response body kosong"))
                    }
                } else {
                    emit(Result.Error(message = "Gagal mendapatkan list member: ${response.message()}"))
                }

            } catch (e: Exception) {
                Log.e("MemberRepo", "Exception: ${e.message}")
                emit(Result.Error(message = e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    }

    override fun getDraftMemberList(): Flow<List<Member>> {
        return flow {
            localDataSource.getMembers().collect { entities ->
                emit(entities.toDomain())
            }
        }
    }

    override fun uploadMember(member: Member): Flow<Result<Member>> {
        return flow {
            emit(Result.Loading)
            try {
                val fields = member.toMultipartMap()
                val ktpFile = uriParse.uriToFile(member.ktpFilePath)
                val ktpFileSecondary = uriParse.uriToFile(member.ktpFileSecondaryPath)

                if (ktpFile == null) {
                    emit(Result.Error(message = "Gagal membaca file KTP"))
                    return@flow
                }
                if (ktpFileSecondary == null) {
                    emit(Result.Error(message = "Gagal membaca file KTP sekunder"))
                    return@flow
                }

                val ktpPart = MultipartBody.Part.createFormData(
                    "ktp_file",
                    ktpFile.name,
                    ktpFile.asRequestBody(uriParse.getMimeType(ktpFile).toMediaType())
                )
                val ktpSecondaryPart = MultipartBody.Part.createFormData(
                    "ktp_file_secondary",
                    ktpFileSecondary.name,
                    ktpFileSecondary.asRequestBody(uriParse.getMimeType(ktpFileSecondary).toMediaType())
                )

                val response = apiService.uploadMember(fields, ktpPart, ktpSecondaryPart)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ktpFile.delete()
                        ktpFileSecondary.delete()
                        localDataSource.deleteMember(body.member.id ?: 0)
                        emit(Result.Success(body.member.toDomain()))
                    } else {
                        emit(Result.Error(message = "Response body kosong"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    emit(Result.Error(message = "Upload gagal [${response.code()}]: $errorBody"))
                }

            } catch (e: Exception) {
                emit(Result.Error(message = e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


}
