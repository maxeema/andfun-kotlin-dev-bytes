package maxeem.america.devbytes.util

class Consumable<T>(private var data: T?) {

    fun consume() = data?.also { data = null }

}