package mcgyver5.ct_scanner;

import mcgyver5.ct_scanner.model.SubDomain;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.PrintWriter;
import java.util.List;

public class DomainTableModel extends AbstractTableModel {
    final int ID_COL = 0;
    final int SUB_DOMAIN_COL = 1;
    final int DNS_RESOLVES_COL = 2;
    final int HTTP_COL = 3;
    final int CHECKED_COL = 4;
    private final CTController controller;
    String[] columns = new String[]{"ID", "Subdomain", "DNS Resolves","HTTP Status","Checked"};
    List<SubDomain> data;

    public DomainTableModel(CTController controller) {
        this.controller = controller;
    }

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
            case ID_COL:
                return subDomain.getId();
            case SUB_DOMAIN_COL:
                return subDomain.getDomain();
            case DNS_RESOLVES_COL:
                return subDomain.getIPAddress();
            case HTTP_COL:
                return subDomain.getHttpStatus();
            case CHECKED_COL:

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

    public List<SubDomain> getData(){
        return this.data;
    }

    public void getUpdatesFromDataKeeper() {
        this.data = controller.getData();
        this.fireTableDataChanged();

    }
}
