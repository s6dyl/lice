package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.test.assertTrue

/**
 * Demos for language feature
 * Created by ice1000 on 2017/4/1.
 *
 * @author ice1000
 */

@TestOnly
class FeatureTest {

	/**
	 * test for string literals
	 */
	@Test(timeout = 1000)
	fun test1() {
		assertEquals("fuck", Lice.run("""
(print "fuck")
"""))
		assertEquals("lover~lover~fucker~fucker~", Lice.run(""""lover~lover~fucker~fucker~""""))
	}

	/**
	 * test for int literals
	 */
	@Test(timeout = 1000)
	fun test2() {
		assertEquals(233666, Lice.run("""
(print 233666)
"""))
		assertEquals(233666, Lice.run("233666"))
	}

	/**
	 * test for plus
	 */
	@Test(timeout = 1000)
	fun test3() {
		assertEquals(7, Lice.run("""
(+ 1 (+ 1 2 3))
"""))
	}

	/**
	 * test for big integers
	 */
	@Test(timeout = 1000)
	fun test4() {
		assertEquals(BigInteger("10000000000000000000000000000233"), Lice.run("""
(+ 10000000000000000000000000000000N 233)
"""))
	}

	/**
	 * test for big decimals
	 */
	@Test(timeout = 1000)
	fun test5() {
		assertEquals(BigDecimal("10000000000000000000000000000000.233"), Lice.run("""
(+ 10000000000000000000000000000000N 0.233)
"""))
	}

	/**
	 * comparision
	 */
	@Test(timeout = 1000)
	fun test6() {
		assertTrue(true == Lice.run("(>= 9 8 7 7 6 6 6 5 4 3 1 -1)"))
		assertTrue(true != Lice.run("(>= 9 8 7 7 6 6 6 5 8 3 1 -1)"))
	}

	/**
	 * string connection
	 */
	@Test(timeout = 1000)
	fun test7() {
		assertEquals("boyNextDoor", Lice.run("""
(str-con "boy" "Next" "Door")
"""))
	}

	/**
	 * parsing string
	 */
	@Test(timeout = 1000)
	fun test8() {
		assertEquals(0xDBE, Lice.run("""
(str->int "0xDBE")
"""))
	}

	/**
	 * string evaluation
	 */
	@Test(timeout = 1000)
	fun test9() {
		assertEquals(2, Lice.run("""
(eval "(+ 1 1)")
"""))
	}

	/**
	 * run/begin block
	 */
	@Test(timeout = 1000)
	fun test10() {
		assertEquals(4, Lice.run("""
(|> (+ 1 1) (+ 2 2))
"""))
	}

	/**
	 * force running
	 */
	@Test(timeout = 1000)
	fun test11() {
		assertNull(Lice.run("""
(force|> (+ () ()))
"""))
		assertNull(Lice.run("()"))
	}

	/**
	 * variable
	 */
	@Test(timeout = 1000)
	fun test12() {
		assertEquals(233, Lice.run("""
(-> ice1000 233)

ice1000
"""))
	}

	/**
	 * function
	 */
	@Test(timeout = 1000)
	fun test13() {
		assertEquals(233, Lice.run("""
(defexpr ice1000 233)

ice1000
"""))
		//language=TEXT
		assertEquals(233, Lice.run("""
(defexpr ice1000 a a)

(ice1000 233)
"""))
	}

	/**
	 * recursion
	 */
	@Test(timeout = 1000)
	fun test14() {
		//language=TEXT
		assertEquals(5, Lice.run("""
(def gcd a b (if (=== b 0) a (gcd b (% a b))))

(gcd 15 20)
"""))
		//language=TEXT
		assertEquals(89, Lice.run("""
(def fib n (if (in? (list 0 1) n)
               1
               (+ (fib (- n 1)) (fib (- n 2)))))

(fib 10)
"""))
	}

	/**
	 * if condition
	 */
	@Test(timeout = 1000)
	fun test15() {
		assertEquals(1, Lice.run("(if (>= 9 8 7 7 6 6 6 5 4 3 1 -1) 1 (+ 1 1))"))
		assertEquals(2, Lice.run("(if (>= 9 8 7 7 6 6 6 5 8 3 1 -1) 1 (+ 1 1))"))
	}

	/**
	 * when condition
	 */
	@Test(timeout = 1000)
	fun test16() {
		assertEquals(666, Lice.run("""(when
(!== 1 1), 233
(=== 1 1), 666
)"""))
		assertEquals(123, Lice.run("""(when
(!== 1 1), 233
(=== 2 1), 666
123
)"""))
	}

	/**
	 * while loop
	 */
	@Test(timeout = 1000)
	fun test17() {
		assertEquals(9, Lice.run("""
(def exp-mod a b m (|>
  (-> ret 1)

  (while (!= 0 b) (|>

    (if (!= 0 (& b 1))
      (-> ret (% (* a ret) m)))
    (-> b (/ b 2))
    (-> a (% (* a a) m))))
  ret))

(exp-mod 23 2 26)
"""))
	}

	/**
	 * lazy evaluation
	 */
	@Test(timeout = 1000)
	fun test18() {
		assertEquals(233, Lice.run("""
(defexpr fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
"""))
		assertEquals(666, Lice.run("""
(def fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
"""))
		//language=TEXT
		assertEquals(233 + 1, Lice.run("""
(defexpr fuck a b (|> b b))

(-> lover 233)

(fuck () (-> lover (+ lover 1)))
"""))
	}

	/**
	 * assignment
	 */
	@Test(timeout = 1000)
	fun test19() {
		assertEquals(233, Lice.run("""
(-> ice1k 233)

(<-> ice1k 666)
ice1k
"""))
		assertNull(Lice.run("""
(-> ice1k null)

(<-> ice1k 666)
ice1k
"""))
		assertEquals(666, Lice.run("""
(<-> ice1k 666)
ice1k
"""))
	}

	/**
	 * literals
	 */
	@Test(timeout = 1000)
	fun test20() {
		assertNull(Lice.run("null"))
		assertTrue(true == Lice.run("true"))
		assertTrue(false == Lice.run("false"))
	}

	/**
	 * escape
	 */
	@Test(timeout = 1000)
	fun test21() {
		assertEquals("\n", Lice.run(""""\n""""))
		assertEquals("\r", Lice.run(""""\r""""))
		assertEquals("\b", Lice.run(""""\b""""))
		assertEquals("\t", Lice.run(""""\t""""))
// FIXME assertEquals("\"", Lice.run(""""\"""""))
		assertEquals("\'", Lice.run(""""\'""""))
		assertEquals("\\", Lice.run(""""\\""""))
	}

	/**
	 * trigonometric
	 */
	@Test(timeout = 1000)
	fun test22() {
		val r = Random(System.currentTimeMillis()).nextDouble()
		assertEquals(Math.sin(r), Lice.run("(sin $r)") as Double, 1e-10)
	}

	/**
	 * head
	 */
	@Test(timeout = 1000)
	fun test23() {
		assertEquals(233, Lice.run("(head (list 233 344 455 566))"))
		assertEquals(233, Lice.run("(head ([|] 233 344 455 566))"))
	}
}
