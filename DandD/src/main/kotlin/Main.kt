import kotlin.random.Random
import kotlin.random.nextInt

//Класс для броска кубиков
class Cube (private val sides: Int = 6){
    fun roll():Int{
        return Random.nextInt(1,sides+1)
    }
}

//Базовай класс для существ
open class Creature(
    val name: String,
    var attack: Int,
    var defence: Int,
    var health: Int,
    var damageSpread: IntRange
) {

    //Обрабатываем параметры пользователя
    init {
        require(attack in 1..30) { "Атака должна быть от 1 до 30" }
        require(defence in 1..30) { "Защита должна быть от 1 до 30" }
        require(health >= 0) { "Здоровье не может быть отрицательным" }
    }

    //Функция для вычисления модификатора атаки
    fun calculateModAttack(target: Creature): Int {
        if (target.defence <= attack) {
            return attack - target.defence + 1
        }
        else{
            return 1
        }
    }

    //Функция для выбора произвольного значения урона
    fun attackDamageSelection(range: IntRange):Int{
        return range.random()
    }

    //Рассчет нанесенного ущерба)
    fun takeDamage(damage:Int){
        health -= damage
        if (health < 0) health = 0
        if (health == 0) println("$name был повержен!")
    }

    //Функция для нападения
    fun attack(target: Creature, cube: Cube) {
        val modAttack = calculateModAttack(target)
        var rolls = mutableListOf<Int>()

        //Бросаем кубик N раз и записываем результаты бросков в список
        for (i in 1..modAttack) {
            rolls.add(cube.roll())
        }

        //Проверка успешности атаки
        val successfulOfAttack: Boolean = (5 in rolls) || (6 in rolls)

        //Оповещение о результатах нападения
        if (successfulOfAttack){
            val damage = attackDamageSelection(damageSpread)
            println("$name успешно атакует ${target.name} и наносит $damage урона")
            target.takeDamage(damage)
        }
        else{
            println("${target.name} защищается от атаки $name, урон не проходит")
        }
    }
}

//Класс игрока, наследованный от существа
class Player(
    name: String,
    attack: Int,
    defense: Int,
    health: Int,
    damageSpread: IntRange
) : Creature(name, attack, defense, health, damageSpread) {
    private var healCount = 4
    private var heal = (health*0.3).toInt()

    //Функция для восстановления здоровья
    fun heal(){
        if (healCount > 0){
            health += heal
            println("$name исцелился на $heal здоровья")
            healCount--
        }
        else{
            println("$name больше не можен исцеляться")
        }
    }
}

// Класс монстра, наследованный от существа
class Monster(
    name: String,
    attack: Int,
    defense: Int,
    health: Int,
    damageSpread: IntRange
) : Creature(name, attack, defense, health, damageSpread)

fun main() {
    val player = Player("King Arthur",9,5,10,1..6)
    val monster = Monster("Zombie wizard",10,7,16,2..7)
    val cube = Cube()

    while (player.health > 0 && monster.health > 0){
        monster.attack(player, cube)
        player.attack(monster, cube)
        player.heal()
        println("Здоровье ${player.name}: ${player.health}, Здоровье ${monster.name}: ${monster.health}")
        println()
    }
    println("Игра закончилась")
}