package vm.words.ua

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform