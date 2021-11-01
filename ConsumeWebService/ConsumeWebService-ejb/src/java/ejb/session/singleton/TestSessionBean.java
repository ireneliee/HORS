/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import ws.client.Record;
import ws.client.RecordEntityWebService_Service;
import ws.client.RecordVersion;

/**
 *
 * @author irene
 */
@Singleton
@LocalBean
@Startup
public class TestSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
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
