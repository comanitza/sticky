package ro.comanitza.sticky.service

import org.junit.{Before, Ignore, Test}
import ro.comanitza.sticky.dto.User

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
}
