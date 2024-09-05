package com.cistus.hunter

class EmailAddress(private val emailAddress: String) {
    fun isValid(): Boolean {
        if (emailAddress.count { it == '@' } != 1)
            return false
        val name = emailAddress.split("@")[0]
        val domainName = emailAddress.split("@")[1]
        if (name.isEmpty() || name.isBlank() ||
            domainName.isEmpty() || domainName.isBlank() ||
            domainName.count { it == '.' } == 0)
            return false
        val domain = domainName.substring((domainName.indexOfLast { it == '.' } + 1)..<domainName.length)
        return !(domain.isEmpty() || domain.isBlank())
    }
    fun emailAddress(): String = emailAddress
}