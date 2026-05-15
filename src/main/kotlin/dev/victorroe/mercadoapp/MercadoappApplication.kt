package dev.victorroe.mercadoapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.TimeZone

@SpringBootApplication
@EnableScheduling
class MercadoappApplication

fun main(args: Array<String>) {
	TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"))
	runApplication<MercadoappApplication>(*args)
}
