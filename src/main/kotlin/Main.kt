//The Q value for Kyber (ML-KEM). This is a prime number ((2^8 * 13) + 1)
const val n = 3329
//2^k. 2 * Q fits into 24 bits.
//However, the comprehensiveTest() function shows that it does not cover all values. 26 bits are needed.
//You may force 24 bits, but you have to catch cases where the resulting value equals -1 and add n(3329).
//The former is better than the latter because of the avoidance of branching code(if statements).
const val shift = 26
//Apply 2^k. k = shift
const val shifted = 1L shl shift
//Precomputed value.
//This is the approximation. Saving this somewhere saves immense process time the more Barrett Reduction is used.
const val m = ((shifted + (n / 2)) / n).toShort()

fun main() {
    println("Short Int Barrett Reduction in Kotlin")
    println("Author: Ron Lauren Hombre\n")

    //Simple Test Case
    println("Modulo : " + (32105 % n))
    println("Barrett: " + barrettReduce(32105))

    //Comprehensive Test Case
    comprehensiveTest()
}

fun comprehensiveTest() {
    //0 up to 32768 (excluding 32768).
    println("Running comprehensive test [0, 32768)...")
    var count = 0
    var correct = 0
    //Traverse all positive Short values.
    for(i in 0..<Short.MAX_VALUE.toInt()) {
        count++

        val certain = (i % n).toShort()
        val guess = barrettReduce(i.toShort())

        if(guess == certain) correct++
        else println("$i/$certain/$guess")
    }

    println("Ratio(Correct/Total/Ratio): $correct/$count/" + (correct/count.toDouble() * 100) + "%")
}

/*
 * Barrett Reduction Algorithm
 * n = Q = 3329
 * - This algorithm works best for modulo operations involving a constant n(also called Q), where the approximation can be saved.
 *
 * Source: Barrett reduction. (2023, August 13). In Wikipedia. https://en.wikipedia.org/wiki/Barrett_reduction
 */
fun barrettReduce(a: Short): Short {
    val q = (a.toInt() * m) shr shift

    return (a - (q * n)).toShort()
}

/*
 * For further reading:
 * Barrett, P. (1986).
 * "Implementing the Rivest Shamir and Adleman Public Key Encryption Algorithm on a Standard Digital Signal Processor".
 * Advances in Cryptology – CRYPTO' 86. Lecture Notes in Computer Science. Vol. 263. pp. 311–323.
 * doi:10.1007/3-540-47721-7_24. ISBN 978-3-540-18047-0.
 */