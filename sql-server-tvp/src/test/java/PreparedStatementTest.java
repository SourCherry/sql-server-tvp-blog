import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import org.junit.Test;
import sql.ConnectionProvider;
import sql.DataRecordProvider;
import sql.RecordColumns;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidrichardson on 09/08/2016.
 */
public class PreparedStatementTest {

    private static final String QUERY =  "select id, [name]\nfrom ?;";

    @Test
    public void SQLDataTableTest() {
        Connection connection = ConnectionProvider.getConnection();
        try(SQLServerPreparedStatement preparedStatement = (SQLServerPreparedStatement) connection.prepareStatement(QUERY)) {
            SQLServerDataTable dataTable = new SQLServerDataTable();
            dataTable.addColumnMetadata("id", Types.BIGINT);
            dataTable.addColumnMetadata("name", Types.VARCHAR);
            dataTable.addRow(1, "the first row");
            dataTable.addRow(2, "the second row");
            preparedStatement.setStructured(1, "dbo.my_first_table_variable", dataTable);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                MyDataRow row = new MyDataRow(resultSet.getInt(1), resultSet.getString(2));
                System.out.println(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ISQLServerDataRecordTest(){

        Connection connection = ConnectionProvider.getConnection();
        List<MyDataRow> sourceData = new ArrayList<>();
        sourceData.add(new MyDataRow(1, "the first row"));
        sourceData.add(new MyDataRow(2, "the second row"));
        try (SQLServerPreparedStatement preparedStatement = (SQLServerPreparedStatement) connection.prepareStatement(QUERY)) {
            RecordColumns<MyDataRow> columns = new RecordColumns<>();
            columns.addColumn(l -> l.getId(), "id", Types.BIGINT, 0, 0);
            columns.addColumn(l -> l.getName(), "name", Types.VARCHAR, 255, 0);
            DataRecordProvider<MyDataRow> provider = new DataRecordProvider<>(columns, sourceData);
            preparedStatement.setStructured(1, "dbo.my_first_table_variable", provider);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                MyDataRow row = new MyDataRow(resultSet.getInt(1), resultSet.getString(2));
                System.out.println(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private class MyDataRow{
        private final int id;
        private final String name;

        private MyDataRow(int id, String name) {
            this.id = id;
            this.name = name;
        }

        int getId() {
            return id;
        }

        String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.valueOf(id) + " : " + name;
        }
    }

}
