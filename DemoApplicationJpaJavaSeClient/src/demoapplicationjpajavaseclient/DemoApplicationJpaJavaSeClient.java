/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapplicationjpajavaseclient;

import java.util.List;
import ws.client.Record;
import ws.client.RecordEntityWebService_Service;
import ws.client.RecordVersion;

/**
 *
 * @author irene
 */
public class DemoApplicationJpaJavaSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RecordEntityWebService_Service service = new RecordEntityWebService_Service();
        List<Record> records = service.getRecordEntityWebServicePort().retrieveAllRecords();
        
        for(Record record : records) {
            System.out.println("Record: " + record.getRecordName());
            System.out.println("currentVersion: "  + record.getCurrentRecordVersion().getRecordVersionDescription());
            
            for(RecordVersion recordVersion: record.getRecordVersions()) {
                System.out.println("recordVersion: " + recordVersion.getRecordVersionDescription());
            } 
        }
    }
    
}
