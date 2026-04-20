package com.reggya.registeroffline.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.domain.usecase.GetDraftMemberUseCase
import com.reggya.registeroffline.domain.usecase.GetMemberListUseCase
import com.reggya.registeroffline.domain.usecase.RegisterMemberUseCase
import com.reggya.registeroffline.domain.usecase.UploadMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import  com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.presentation.utils.UiState
import com.reggya.registeroffline.presentation.utils.UiState.*

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val getMemberListUseCase: GetMemberListUseCase,
    private val getDraftMemberUseCase: GetDraftMemberUseCase,
    private val uploadMemberUseCase: UploadMemberUseCase
) : ViewModel() {

    private val _draftMemberList = MutableStateFlow<UiState<List<Member>>?>(null)
    val draftMemberList: StateFlow<UiState<List<Member>>?> = _draftMemberList

    private val _memberList = MutableStateFlow<UiState<List<Member>>?>(null)
    val memberList: StateFlow<UiState<List<Member>>?> = _memberList

    private val _uploadAllState = MutableStateFlow<UiState<String>?>(null)
    val uploadAllState: StateFlow<UiState<String>?> = _uploadAllState

    init {
        getDraftMemberList()
    }
    fun registerMember(member: Member) {
        viewModelScope.launch {
            registerMemberUseCase(member)
            getDraftMemberList()
        }
    }

    fun getDraftMemberList() {
        viewModelScope.launch {
            getDraftMemberUseCase().collect { members ->
                _draftMemberList.value = Success(members)
            }
        }
    }

    fun getMemberList() {
        viewModelScope.launch {
            getMemberListUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _memberList.value = Success(result.data)
                    }

                    is Result.Error -> {
                        _memberList.value = Error(result.message ?: "Unknown error")
                    }

                    is Result.Loading -> {
                        _memberList.value = Loading
                    }
                }
            }
        }
    }

    fun uploadMember(member: Member) {
        viewModelScope.launch {
            uploadMemberUseCase(member).collect { result ->
                when (result) {
                    is Result.Success -> {
                        getDraftMemberList()
                        _uploadAllState.value = Success("Berhasil Upload")
                    }

                    is Result.Error -> {
                        _uploadAllState.value = Error("Gagal Upload ${result.message}")
                    }

                    is Result.Loading -> {
                        _uploadAllState.value = Loading
                    }
                }
            }
        }
    }

    fun uploadAll() {
        viewModelScope.launch {
            _uploadAllState.value = Loading

            try {
                val members = _draftMemberList.value

                when (members) {
                    is Loading -> {
                        _uploadAllState.value = Loading
                        return@launch
                    }
                    is Error -> {
                        _uploadAllState.value = Error(members.message)
                        return@launch
                    }
                    is Success -> {
                        if (members.data.isNullOrEmpty()) {
                            _uploadAllState.value = Error("Tidak ada data draft")
                            return@launch
                        }
                        members.data.forEach { member ->
                            uploadMember(member)
                        }

                    }
                    null -> {
                        _uploadAllState.value = Error("Tidak ada data draft")
                        return@launch
                    }
                }
            } catch (e: Exception) {
                _uploadAllState.value = Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}