package uk.ac.warwick.camcat.helpers

object RomanNumerals {
  // Original: https://www.rosettacode.org/wiki/Roman_numerals/Encode#Kotlin

  private val romanNumerals = mapOf(
    1000 to "M",
    900 to "CM",
    500 to "D",
    400 to "CD",
    100 to "C",
    90 to "XC",
    50 to "L",
    40 to "XL",
    10 to "X",
    9 to "IX",
    5 to "V",
    4 to "IV",
    1 to "I"
  )

  fun encode(number: Int): String {
    require(number in 1..5000) { "Cannot convert $number to Roman numerals" }
    var num = number
    var result = ""
    for ((multiple, numeral) in romanNumerals.entries) {
      while (num >= multiple) {
        num -= multiple
        result += numeral
      }
    }
    return result
  }
}
