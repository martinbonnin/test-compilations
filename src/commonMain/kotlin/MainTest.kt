import kotlin.test.Test

class MainTest {
    @Test
    fun test() {
        check(SomeClass().hello() == "Hello")
        check(platform() == 42)
    }
}