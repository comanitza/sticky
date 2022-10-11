package ro.comanitza.sticky.service

import org.junit.{Ignore, Test}
import ro.comanitza.sticky.dto.User

@Ignore
class SqliteDaoTest {

  @Test
  def testTableCreation(): Unit = {

    val dao: Dao = new SqliteDao("D:\\sbin\\dbs\\sticky.db")

    dao.createTables()

    dao.createUser(new User(name = "gigel2", pass="shaorma", email = "gigi2@nsa.gov"))

  }
}
