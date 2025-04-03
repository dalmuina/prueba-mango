package com.dalmuina.pruebamango.core.presentation

import com.dalmuina.pruebamango.core.domain.NetworkError

sealed interface NetworkErrorEvent {
    data class Error(val error: NetworkError): NetworkErrorEvent
}
