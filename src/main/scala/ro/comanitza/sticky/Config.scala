package ro.comanitza.sticky

import javax.servlet.FilterRegistration
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.{Bean, Configuration}
import ro.comanitza.sticky.filters.{AdminFilter, LogInRedirectFilter, LogInRejectFilter}
import ro.comanitza.sticky.service.{CookieGuestIdEncoder, CookieGuestIdEncoderImpl, CookiesService, Dao, ExceptionService, SqliteDao, StickiesService, UsersService}

@Configuration
class Config {

  @Bean
  def exceptionService(@Value("${exception.service.buffer.size:20}") bufferSize: Int): ExceptionService = {
    new ExceptionService(bufferSize)
  }

  @Bean
  def dao(@Value("${sticky.sqlite.file.path}") sqliteFilePath: String): Dao = {
    val dao = new SqliteDao(sqliteFilePath)

    /**
     * init the DAO
     */
    dao.init()

    dao
  }

  @Bean
  @Autowired
  def usersService(dao: Dao, exceptionService: ExceptionService): UsersService = {
    new UsersService(dao, exceptionService)
  }

  @Bean
  @Autowired
  def stickiesService(dao: Dao, exceptionService: ExceptionService): StickiesService = {
    new StickiesService(dao, exceptionService)
  }

  @Bean
  def loginFilterRejectRegistration(): FilterRegistrationBean[LogInRejectFilter] = {

    val registration = new FilterRegistrationBean[LogInRejectFilter]

    registration.setFilter(new LogInRejectFilter())
    registration.addUrlPatterns("/rest", "/rest/*")

    registration.setOrder(3)

    registration
  }

  @Bean
  def loginFilterRedirectRegistration(): FilterRegistrationBean[LogInRedirectFilter] = {

    val registration = new FilterRegistrationBean[LogInRedirectFilter]

    registration.setFilter(new LogInRedirectFilter())
    registration.addUrlPatterns("/stickies", "/stickies/*", "/sticky", "/sticky/*")

    registration.setOrder(2)

    registration
  }

  @Bean
  def adminFilterRegistration(): FilterRegistrationBean[AdminFilter] = {

    val registration = new FilterRegistrationBean[AdminFilter]

    registration.setFilter(new AdminFilter())
    registration.addUrlPatterns("/admin", "/admin/*")

    registration.setOrder(2)

    registration
  }

  @Bean
  @Autowired
  def cookiesService(usersService: UsersService): CookiesService = {

    new CookiesService(usersService)
  }

  @Bean
  def cookieGuestIdEncoder(): CookieGuestIdEncoder = {
    new CookieGuestIdEncoderImpl()
  }
}
