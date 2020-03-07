package com.pranjaldesai.coronavirustracker.lifecycle

enum class ApplicationState {
    UNKNOWN(),
    CREATED(),
    MOVING_TO_BACKGROUND(),
    IN_BACKGROUND(),
    MOVING_TO_FOREGROUND(),
    IN_FOREGROUND()
}