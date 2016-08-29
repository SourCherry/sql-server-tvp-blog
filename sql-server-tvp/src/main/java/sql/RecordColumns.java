package sql;

import com.microsoft.sqlserver.jdbc.SQLServerMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by davidrichardson on 07/06/2016.
 */
public class RecordColumns<T> {

    private final List<DataColumn> columns = new ArrayList<>();

    /**
     * method = the method to call to get the column data, for example BigDecimal::toString()
     * name = name of the column
     * type = java.bulkcopyperformance.sql.Types
     */

    public RecordColumns addColumn(Function<T, Object> methodReference, String name, int type, int precision, int scale){
        DataColumn dataColumn = new DataColumn(methodReference, name, type, precision, scale);
        columns.add(dataColumn);
        return this;
    }

    Object[] getRecord(T data){
        final Object[] row = new Object[columns.size()];
        for (int i = 0; i < row.length; i++) {
            row[i] = columns.get(i).applyMethod(data);
        }
        return row;
    }

    int getNumberOfColumns(){
        return columns.size();
    }

    SQLServerMetaData getColumnMetaData(int ordinal){
        return columns.get(ordinal);
    }

    private class DataColumn <U> extends SQLServerMetaData {

        private final Function<U, Object> methodReference;

        DataColumn(Function<U, Object> methodReference, String name, int sqlType, int precision, int scale) {
            super(name, sqlType, precision, scale);
            this.methodReference = methodReference;
        }

        Object applyMethod(U data){
            return methodReference.apply(data);
        }

    }

}
