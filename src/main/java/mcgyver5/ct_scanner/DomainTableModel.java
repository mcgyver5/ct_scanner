package mcgyver5.ct_scanner;

import mcgyver5.ct_scanner.model.SubDomain;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.PrintWriter;
import java.util.List;

public class DomainTableModel extends AbstractTableModel {
    int ID_COL = 0;
    int SUB_DOMAIN_COL = 1;
    int DNS_RESOLVES_COL = 2;
    int HTTP_COL = 3;
    int CHECKED_COL = 4;
    String[] columns = new String[]{"ID", "Subdomain", "DNS Resolves","HTTP Status","Checked"};
    List<SubDomain> data;

    @Override
    public int getRowCount() {
        return data.size();
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
        if(columnIndex == this.CHECKED_COL){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SubDomain subDomain = data.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return subDomain.getId();
            case 1:
                return subDomain.getDomain();
            case 2:
                return subDomain.getIPAddress();
            case 3:
                return subDomain.getHttpStatus();
            case 4:
                return subDomain.isSelected();
        }
            return null;

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        SubDomain sd = data.get(rowIndex);
        if(columnIndex == this.CHECKED_COL){
            sd.setSelected((boolean)aValue);
        }
        fireTableCellUpdated(rowIndex,columnIndex);
    }


    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    public void setData(List<SubDomain> domains){
        this.data = domains;
    }
}
