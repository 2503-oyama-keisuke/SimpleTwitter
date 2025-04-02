package chapter6.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;

import chapter6.beans.User;
import chapter6.utils.DBUtil;
import junit.framework.TestCase;

// 1. テストクラス名は任意のものに変更してください。
// 2. L.23~86は雛形として使用してください。
// 3．L.44のファイル名は各自作成したファイル名に書き換えてください。
public class UserServiceTest extends TestCase {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/simple_twitter";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public UserServiceTest() {
		super();
	}

	private File file;

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		return connection;
	}

	@Before
	public void setUp() throws Exception {

		IDatabaseConnection connection = null;
		try {
			Connection conn = DBUtil.getConnection();
			connection = new DatabaseConnection(conn);

			//(2)現状のバックアップを取得
			QueryDataSet partialDataSet = new QueryDataSet(connection);
			partialDataSet.addTable("users");
			partialDataSet.addTable("messages");

			file = File.createTempFile("temp", ".xml");
			FlatXmlDataSet.write(partialDataSet,
					new FileOutputStream(file));

			//(3)テストデータを投入する
			IDataSet dataSetUser = new FlatXmlDataSet(new File("user_test_data1.xml"));
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSetUser);

			DBUtil.commit(conn);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		IDatabaseConnection connection = null;
		try {
			Connection conn = DBUtil.getConnection();
			connection = new DatabaseConnection(conn);

			IDataSet dataSet = new FlatXmlDataSet(file);
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

			DBUtil.commit(conn);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			//一時ファイルの削除
			if (file != null) {
				file.delete();
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}

		}
	}

	// 以下に課題のテストメソッドを作成

	public void testSelectUser() throws Exception {

		//参照メソッドの実行
		UserService userService = new UserService();
		User result = userService.select("a", "a");

		//値の検証

		//件数

		//データ
		assertEquals("id=1", "id=" + result.getId());
		assertEquals("account=a", "account=" + result.getAccount());
		assertEquals("name=a", "text=" + result.getName());
		assertEquals("email=a", "email=" + result.getEmail());
		assertEquals("password=a", "password=" + result.getPassword());
	}

	public void testInsertUser() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		User user = new User();
		user.setAccount("b");
		user.setName("b");
		user.setEmail("b");
		user.setPassword("b");

		UserService userService = new UserService();
		userService.insert(user);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("user_test_data2.xml"));
			ITable expectedTable = expectedDataSet.getTable("users");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	public void testUpdateUser() throws Exception {

		//テスト対象となる、storeメソッドを実行
		//テストのインスタンスを生成
		User user = new User();
		user.setId(1);
		user.setAccount("c");
		user.setName("c");
		user.setEmail("c");
		user.setPassword("c");

		UserService userService = new UserService();
		userService.update(user);

		//テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
		IDatabaseConnection connection = null;
		try {

			Connection conn = getConnection();
			connection = new DatabaseConnection(conn);

			//メソッド実行した実際のテーブル
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("users");

			// テスト結果として期待されるべきテーブルデータを表すITableインスタンスを取得
			IDataSet expectedDataSet = new FlatXmlDataSet(new File("user_test_data3.xml"));
			ITable expectedTable = expectedDataSet.getTable("users");

			//期待されるITableと実際のITableの比較
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			if (connection != null)
				connection.close();
		}
	}

}