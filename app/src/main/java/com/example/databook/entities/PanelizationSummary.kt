package com.example.databook.entities

import java.io.Serializable

data class PanelizationSummary(
    val containsEpubBubbles: Boolean = false,
    val containsImageBubbles: Boolean = false
): Serializable