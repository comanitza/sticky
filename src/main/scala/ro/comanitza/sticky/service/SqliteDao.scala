package ro.comanitza.sticky.service
import java.sql.{Connection, DriverManager, Statement}

import org.slf4j.{Logger, LoggerFactory}
import ro.comanitza.sticky.SUtils
import ro.comanitza.sticky.dto.{Note, Sticky, User}

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

      val createStickiesTable =
        """CREATE TABLE IF NOT EXISTS stickies
          (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          userId INTEGER NOT NULL,
          content TEXT NOT NULL,
          posX INTEGER NOT NULL,
          posY INTEGER NOT NULL,
          category TEXT DEFAULT 'default',
          created DATETIME DEFAULT CURRENT_TIMESTAMP)""".stripMargin

      statement.executeUpdate(createStickiesTable)

      val createNotesTable =
        """CREATE TABLE IF NOT EXISTS notes
          (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          stickyId INTEGER NOT NULL,
          content TEXT NOT NULL,
          created DATETIME DEFAULT CURRENT_TIMESTAMP
          )""".stripMargin

      statement.executeUpdate(createNotesTable)

    } finally {
      SUtils.closeQuietly(statement)
    }
  }

  override def createUser(user: User): Either[Exception, Boolean] = {

    val insertUser = s"insert into users (name, pass, email) values('${user.name}', '${user.pass}', '${user.email}')"

    genericInsert(insertUser)
  }

  override def fetchUserByUsername(userName: String, pass: String): Either[Exception, Option[User]] = {

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val result = statement.executeQuery(s"SELECT * from users where name = '$userName' and pass = '$pass'")

      if (result.next()) {
        Right.apply(Some(new User(
            id = result.getInt("id"),
            name = result.getString("name"),
            pass =  "",
            email = result.getString("email"),
            created = result.getLong("created"), lastLogin = result.getLong("lastLogin")
          )
        )
      )
      } else {
        Right.apply(None)
      }

    } catch {
      case e: Exception => {

        log.error(s"Error fetching user $userName", e)

        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
    }
  }

  override def createSticky(sticky: Sticky, userId: Int): Either[Exception, Boolean] = {

    val insertStickySql = s"insert into stickies (userId, content, posX, posY, category) values($userId, '${sticky.content}', ${sticky.posX}, ${sticky.posY}, '${sticky.category}')"

    genericInsert(insertStickySql)
  }

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

  override def createNote(note: Note, stickyId: Int): Either[Exception, Boolean] = {

    val insertNoteSql = s"insert into notes (stickyId, content) values($stickyId, '${note.content}')"

    genericInsert(insertNoteSql)
  }
}
