package com.example.isbm.Entities

import java.io.Serializable

data class PanelizationSummary(
    val containsEpubBubbles: Boolean = false,
    val containsImageBubbles: Boolean = false
): Serializable