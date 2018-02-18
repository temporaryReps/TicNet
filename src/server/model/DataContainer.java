package server.model;

import java.io.Serializable;

/**
 * Container for data which must be send through socket
 */
public class DataContainer implements Serializable {
    private static final long SerialVersionUID = 879L;

    private String[][] fieldData;
    private String report;

    public DataContainer(String[][] fieldData, String report) {
        this.fieldData = fieldData;
        this.report = report;
    }

    public String[][] getFieldData() {
        return fieldData;
    }

    public String getReport() {
        return report;
    }
}
