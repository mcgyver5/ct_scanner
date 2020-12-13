package mcgyver5.ct_scanner;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.PrintWriter;

public class DomainTableModel extends AbstractTableModel {
    String[] columns = new String[]{"ID", "Subdomain", "DNS Resolves","HTTP Status","Checked"};
    //my temp data:
//    Object[][] data = new Object[][] {
//            {121,"es.python.org","1.10.100.200",200,true},
//            {122,"bugs.python.org","1.10.100.203",200,true},
//            {123,"pypi.python.org","1.10.100.202",200,true},
//            {124,"mail.python.org","1.10.100.201",200,true}
//
//    };
    Object[][] data;

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0,columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex == 4){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex,columnIndex);
    }



    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    public void setData(Object[][] data) {
        this.data = data;
    }
}
