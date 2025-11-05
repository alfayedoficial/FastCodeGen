package com.alfayedoficial.fastcodegen.utils

object StringUtils {

   /**
    * Converts to camelCase
    * ForgetPassword -> forgetPassword
    * UserProfile -> userProfile
    */
   fun toCamelCase(input: String): String {
      if (input.isEmpty()) return input

      val words = input
         .replace(Regex("[^a-zA-Z0-9]"), " ")
         .trim()
         .split(Regex("(?=[A-Z])|\\s+"))
         .filter { it.isNotEmpty() }

      if (words.isEmpty()) return input.lowercase()

      return words.first().lowercase() +
              words.drop(1).joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
   }

   /**
    * Converts to PascalCase
    * forgetPassword -> ForgetPassword
    * forget_password -> ForgetPassword
    */
   fun toPascalCase(input: String): String {
      if (input.isEmpty()) return input

      val words = input
         .replace(Regex("[^a-zA-Z0-9]"), " ")
         .trim()
         .split(Regex("(?=[A-Z])|\\s+"))
         .filter { it.isNotEmpty() }

      if (words.isEmpty()) return input.replaceFirstChar { it.uppercase() }

      return words.joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
   }

   fun toSnakeCase(input: String): String {
      return input
         .replace(Regex("([a-z])([A-Z])"), "$1_$2")
         .lowercase()
   }
}