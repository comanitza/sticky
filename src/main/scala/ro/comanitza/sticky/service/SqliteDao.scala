package ro.comanitza.sticky.service
import java.sql.{Connection, DriverManager, Statement, Timestamp}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.locks.{Lock, ReentrantLock}

import org.slf4j.{Logger, LoggerFactory}
import ro.comanitza.sticky.SUtils
import ro.comanitza.sticky.dto.{Note, Sticky, User}

import scala.collection.mutable.ListBuffer

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
  private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withLocale( Locale.US )

  private val lock: Lock = new ReentrantLock()

  private def createConnection (): Connection = {
    DriverManager.getConnection(s"jdbc:sqlite:$dbPath")
  }

  /**
   * Dao init actions go here
   */
  def init(): Unit = {
    createTables()
  }

  def createTables(): Unit = {

    log.info("### executing table creation")

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val createUsersSql =
        """CREATE TABLE IF NOT EXISTS users
          (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          pass TEXT NOT NULL,
          email TEXT  NOT NULL,
          created DATETIME  DEFAULT CURRENT_TIMESTAMP,
          lastLogin DATETIME  DEFAULT NULL, UNIQUE(email))""".stripMargin

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

  override def createUser(user: User): Either[Exception, Int] = {

    val insertUser = s"insert into users (pass, email) values('${user.pass}', '${user.email}')"

    genericInsert(insertUser)
  }

  override def fetchUserByEmail(email: String, pass: String): Either[Exception, Option[User]] = {

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val result = statement.executeQuery(s"SELECT * from users where email = '$email' and pass = '$pass'")

      if (result.next()) {
        Right.apply(Some(new User(
            id = result.getInt("id"),
            name = "",
            pass =  "",
            email = result.getString("email"),
            created = result.getLong("created"),
            lastLogin = result.getLong("lastLogin")
          )
        )
      )
      } else {
        Right.apply(None)
      }

    } catch {
      case e: Exception => {

        log.error(s"Error fetching user $email", e)

        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
    }
  }

  override def createSticky(sticky: Sticky, userId: Int): Either[Exception, Int] = {

    val insertStickySql = if (sticky.category == null) s"insert into stickies (userId, content, posX, posY) values($userId, '${sticky.content}', ${sticky.posX}, ${sticky.posY})" else s"insert into stickies (userId, content, posX, posY, category) values($userId, '${sticky.content}', ${sticky.posX}, ${sticky.posY}, '${sticky.category}')"

    genericInsert(insertStickySql)
  }

  override def fetchStickiesForUser(userId: Int): Either[Exception, List[Sticky]] = {

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val result = statement.executeQuery(s"select s.* from stickies as s where s.userId = $userId")

      val stickies = new ListBuffer[Sticky]()

      while(result.next()) {

        stickies += new Sticky(
          id = result.getInt("id"),
          content = result.getString("content"),
          posX = result.getInt("posX"),
          posY = result.getInt("posY"),
          category = result.getString("category"),
          created = Timestamp.valueOf(LocalDateTime.parse(result.getString("created"), dateTimeFormatter)).getTime
        )
      }

      val stickyIdsAsString = stickies.map(s => s.id).toList.mkString(",")

      val notesResult = statement.executeQuery(s"select * from notes where stickyId IN ($stickyIdsAsString)")

      val notes = new ListBuffer[Note]()

      while (notesResult.next()) {

        notes += new Note(
          id = notesResult.getInt("id"),
          stickyId = notesResult.getInt("stickyId"),
          content = notesResult.getString("content"),
          created = Timestamp.valueOf(LocalDateTime.parse(result.getString("created"), dateTimeFormatter)).getTime
        )
      }

      val notesAsMap = notes.groupBy(n => n.stickyId)

      for (s <- stickies) {

        if (notesAsMap.contains(s.id)) {
          s.notes = notesAsMap(s.id).toList
        }
      }

      Right.apply(stickies.toList)
    } catch {
      case e: Exception => {

        log.error(s"Error fetching stickies for userId $userId", e)

        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
    }
  }

  def updateLastLogin(userId: Int): Either[Exception, Boolean] = {

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val sql = s"UPDATE users set lastLogin = CURRENT_TIMESTAMP where id=$userId"

      statement.executeUpdate(sql)

      log.info(s"### executing: $sql")

      Right.apply(true)
    } catch {
      case e: Exception => {

        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
    }
  }

  /**
   *
   * @param statementAsString
   * @return
   */
  private def genericInsert(statementAsString: String): Either[Exception, Int] = {

    var statement: Statement = null

    lock.lock()
    try {

      statement = connection.createStatement()

      val sql = statementAsString

      statement.executeUpdate(sql)

      log.info(s"### executing: $sql")

      val idResult = statement.executeQuery("select last_insert_rowid()")


      var insertedId = -1
      if (idResult.next()) {
        insertedId = idResult.getInt(1)
      }

      Right.apply(insertedId)
    } catch {
      case e: Exception => {

        log.error(s"Error executing $statementAsString", e)

        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
      lock.unlock()
    }
  }

  override def createNote(note: Note): Either[Exception, Int] = {

    val insertNoteSql = s"insert into notes (stickyId, content) values(${note.stickyId}, '${note.content}')"

    genericInsert(insertNoteSql)
  }

  class StickyAndNoteRow() {

  }

  override def countRowsInTable(tableName: String): Either[Exception, Int] = {

    var statement: Statement = null

    try {

      statement = connection.createStatement()

      val result = statement.executeQuery(s"select count(*) from $tableName")

      var count = -1
      if (result.next()) {
        count = result.getInt(1)
      }

      Right.apply(count)
    } catch {
      case e: Exception => Left.apply(e)
    }  finally {
      SUtils.closeQuietly(statement)
    }
  }

  override def deleteSticky(userId: Int, stickyId: Int): Either[Exception, Boolean] = {

    var statement: Statement = null
    try {

      statement = connection.createStatement()

      statement.executeUpdate(s"delete from stickies where userId=$userId and id=$stickyId")

      Right.apply(true)
    } catch {
      case e: Exception => {

        log.error("Error deleting sticky", e)
        Left.apply(e)
      }
    } finally {
      SUtils.closeQuietly(statement)
    }
  }

  override def moveSticky(userId: Int, stickyId: Int, posX: Int, posY: Int): Either[Exception, Boolean] = {

   var statement: Statement = null

   try {

     statement = connection.createStatement()

     statement.executeUpdate(s"update stickies set posX=$posX, posY=$posY where userId=$userId and id=$stickyId")

    Right(true)
   } catch {
     case e: Exception => {


       log.error("Error moving sticky {}", stickyId, e)
       Left.apply(e)
     }
   } finally {
     SUtils.closeQuietly(statement)
   }
  }
}
