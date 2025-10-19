package vm.words.ua.navigation


fun SimpleNavController.navigate(screen: Screen) {
    this.navigate(screen.route)
}

fun SimpleNavController.navigateAndClear(screen: Screen) {
    this.navigateAndClear(screen.route)
}

fun SimpleNavController.popBackStackTo(screen: Screen, inclusive: Boolean = false): Boolean{
    return this.popBackStackTo(screen.route, inclusive)
}