package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.modules.AuthApiModule
import cz.cvut.fel.kopecm26.bakaplanner.networking.modules.OrganizationApiModule
import cz.cvut.fel.kopecm26.bakaplanner.networking.modules.SchedulingApiModule
import cz.cvut.fel.kopecm26.bakaplanner.networking.modules.ShiftsApiModule

@JvmSuppressWildcards
interface ApiDescription : AuthApiModule, ShiftsApiModule, OrganizationApiModule, SchedulingApiModule