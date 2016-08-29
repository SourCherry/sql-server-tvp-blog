package sql;

import com.microsoft.sqlserver.jdbc.ISQLServerDataRecord;
import com.microsoft.sqlserver.jdbc.SQLServerMetaData;

import java.util.Iterator;

/**
 * Created by davidrichardson on 07/08/2016.
 */
public class DataRecordProvider<T> implements ISQLServerDataRecord {

    private final Iterator<T> rows;
    private final RecordColumns columns;

    public DataRecordProvider(RecordColumns columns, Iterable<T> rows) {
        this.rows = rows.iterator();
        this.columns = columns;
    }

    @Override
    public SQLServerMetaData getColumnMetaData(int i) {
        return columns.getColumnMetaData(i - 1);
    }

    @Override
    public int getColumnCount() {
        return columns.getNumberOfColumns();
    }

    @Override
    public Object[] getRowData() {
        T item =  rows.next();
        Object[] row = this.apply(item);
        return row;
    }

    @Override
    public boolean next() {
        return rows.hasNext();
    }

    private Object[] apply(T t) {
        return columns.getRecord(t);
    }

}
