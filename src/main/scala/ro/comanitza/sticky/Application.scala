package ro.comanitza.sticky

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 *
 * Entry point for the Sticky App
 *
 * @author stefan.comanita
 */
@SpringBootApplication
class Application {}

object Application {

  def main(args: Array[String]): Unit = {
    println("starting sticky app ...")

    SpringApplication.run(classOf[Application])
  }
}
