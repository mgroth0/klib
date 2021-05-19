package matt.klib.math

import kotlin.math.pow


const val THOUSAND = 1000.0
const val MILLION = THOUSAND*THOUSAND
const val BILLION = THOUSAND*MILLION
const val TRILLION = THOUSAND*BILLION
const val QUADRILLION = THOUSAND*TRILLION

fun Double.sq() = pow(2)
fun Double.cubed() = pow(3)