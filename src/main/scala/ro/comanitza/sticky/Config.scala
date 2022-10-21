package ro.comanitza.sticky

import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.annotation.{Bean, Configuration}
import ro.comanitza.sticky.service.{Dao, SqliteDao, StickiesService, UsersService}

@Configuration
class Config {

  @Bean
  def dao(@Value("${sticky.sqlite.file.path}") sqliteFilePath: String): Dao = {
    val dao = new SqliteDao(sqliteFilePath)

    dao.init()

    dao
  }

  @Bean
  @Autowired
  def usersService(dao: Dao): UsersService = {
    new UsersService(dao)
  }

  @Bean
  @Autowired
  def stickiesService(dao: Dao): StickiesService = {
    new StickiesService(dao)
  }
}
