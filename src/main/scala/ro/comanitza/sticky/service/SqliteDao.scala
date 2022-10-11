package ro.comanitza.sticky.service
import java.sql.{Connection, DriverManager, Statement}

import org.slf4j.{Logger, LoggerFactory}
import ro.comanitza.sticky.SUtils
import ro.comanitza.sticky.dto.{Sticky, User}

/**
 *
 * SQLite implementation of the DAO contract
 *
 * @param dbPath the path to the SQLite file
 * @author stefan.comanita
 */
class SqliteDao(private val dbPath: String) extends Dao {

  private val connection = createConnection()

  private val log: Logger = LoggerFactory.getLogger(classOf[SqliteDao])

  private def createConnection (): Connection = {
    DriverManager.getConnection(s"jdbc:sqlite:$dbPath")
  }

  def createTables(): Unit = {

    log.info("### executing table creation")

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val createUsersSql =
        """CREATE TABLE IF NOT EXISTS users
          (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          name TEXT NOT NULL,
          pass TEXT NOT NULL,
          email TEXT  NOT NULL,
          created DATETIME  DEFAULT CURRENT_TIMESTAMP,
          lastLogin DATETIME  DEFAULT NULL)""".stripMargin

      statement.executeUpdate(createUsersSql)

    } finally {
      SUtils.closeQuietly(statement)
    }

  }

  override def createUser(user: User): Either[Exception, Boolean] = {

    val insertUser = s"insert into users (name, pass, email) values('${user.name}', '${user.pass}', '${user.email}')"

    genericInsert(insertUser)
  }

  override def fetchUserByUsername(userName: String): Either[Exception, User] = {

    null
  }

  override def createSticky(sticky: Sticky): Either[Exception, Boolean] = ???

  override def fetchStickiesForUser(userId: Int): Either[Exception, List[Sticky]] = ???

  private def genericInsert(statementAsString: String): Either[Exception, Boolean] = {

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val sql = statementAsString

      statement.executeUpdate(sql)

      log.info(s"### executing: $sql")

      Right.apply(true)
    } catch {
      case e: Exception => {

        log.error(s"Error executing $statementAsString", e)

        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
    }
  }
}
