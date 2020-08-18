import me.qping.utils.codegen.CodeGenApp;
import me.qping.utils.codegen.bean.h2.DBConnection;
import me.qping.utils.codegen.dao.DBConnectionDao;
import me.qping.utils.codegen.util.SnowFlakeId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeGenApp.class)
public class AppTest {

    @Autowired
    DBConnectionDao dbConnectionDao;

    @Test
    public void test() {

        SnowFlakeId worker = new SnowFlakeId(1,1,1);
//
//        DBConnection conn = new DBConnection();
//        conn.setId(worker.nextId());
//        conn.setName("ETL库");
//        conn.setDatabaseType(DBConnection.TYPE_MYSQL);
//        conn.setHost("192.168.1.201");
//        conn.setPort(30306);
//        conn.setDatabaseName("data_transform");
//        conn.setUsername("root");
//        conn.setPassword("rxthinkingmysql");
//
//        dbConnectionDao.save(conn);



        List<DBConnection> list = dbConnectionDao.findAll();
        System.out.println("size is : " + list.size());
        System.out.println(list);

    }
}
