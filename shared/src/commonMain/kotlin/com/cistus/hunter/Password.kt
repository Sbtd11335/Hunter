package com.cistus.hunter

class Password(private val password: String, private val range: IntRange = 8..64) {
    companion object {
        private val VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~"
        private val NON_ALPHABET_CHARACTERS = "0123456789!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~"
        val OK: UByte = 0u
        val SHORT: UByte = 1u
        val LONG: UByte = 2u
        val ALPHABET_ONLY: UByte = 3u
        val MISSING_ALPHABET: UByte = 4u
        val INVALID_CHARACTERS: UByte = 5u
    }
    fun isValid(): UByte {
        if (password.length < range.first)
            return SHORT
        if (password.length > range.last)
            return LONG
        var getAlphabet = false
        var getNonAlphabet = false
        for (c in password) {
            if (VALID_CHARACTERS.indexOfFirst { it == c } == -1)
                return INVALID_CHARACTERS
            if (NON_ALPHABET_CHARACTERS.indexOfFirst { it == c } >= 0)
                getNonAlphabet = true
            else
                getAlphabet = true
        }
        if (!getNonAlphabet)
            return ALPHABET_ONLY
        if (!getAlphabet)
            return MISSING_ALPHABET
        return OK
    }
    fun getPassword(): String = password
}