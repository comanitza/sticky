package ro.comanitza.sticky.service

import org.junit.{Before, Ignore, Test}
import ro.comanitza.sticky.dto.{Note, Sticky, User}

@Ignore
class SqliteDaoTest {

  private val dao: Dao = new SqliteDao("D:\\sbin\\dbs\\sticky.db")

  @Before
  def createTables(): Unit = {

    println("creating tables... ")
    dao.createTables()
  }

  @Test
  def testInsertAndFetchUser(): Unit = {

    //dao.createUser(new User(name = "gigel2", pass="shaorma", email = "gigi2@nsa.gov"))

    println(dao.fetchUserByUsername("gigel2", "shaorma"))
  }

  @Test
  def testInsertAndFetchSticky(): Unit = {

    //println(dao.createSticky(new Sticky(content = "gigi becali presedinte", posX = 20, posY = -10, category = "work"), 2))

    val stickies = dao.fetchStickiesForUser(2)

    stickies.foreach(println)
  }

  @Test
  def testInsertAndFetchNote(): Unit = {

    dao.createNote(new Note(content = "do this asap!", stickyId = 1))
  }
}
