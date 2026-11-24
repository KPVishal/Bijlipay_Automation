package com.bijlipay.ServiceImplementation;

import com.bijlipay.Exception.FileStorageException;
import com.bijlipay.Service.RentService;
import com.bijlipay.Utils.Constants;
import com.bijlipay.Utils.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class RentServiceImpl implements RentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Modifying
    @Override
    public void calculateRental() {
        List<String> tids = entityManager.createNativeQuery("SELECT Tid FROM rent.dbo.pending WHERE pending > 0").getResultList();
        Date date = DateUtil.convertStringToDate(DateUtil.currentDate("yyyy-MM-dd"), "yyyy-MM-dd");
        String query = "INSERT INTO rent.dbo.collection_details(tid,collected_date,collected) values (:tid,:date,:collected)";
        tids.stream().forEach(ptid -> {
            List<Object[]> daily = entityManager.createNativeQuery("SELECT * FROM rent.dbo.daily WHERE tid = :tid")
                    .setParameter("tid", ptid).getResultList();
            daily.stream().forEach(t -> {
                String tid = t[0].toString();
                System.out.println("tid " + tid);
                List<Object[]> rentTable = entityManager.createNativeQuery("SELECT * FROM rent.dbo.pending WHERE tid = :tid")
                        .setParameter("tid", tid).getResultList();
                rentTable.stream().forEach(r -> {
                    double txnAmount = (double) t[1];
                    double pending = (double) r[2];
                    double collected = 0;
                    double vpend = 0;
                    if (txnAmount >= pending) {
                        System.out.println("greater");
                        collected = pending;
                        System.out.println("collected "+collected);
                        System.out.println("pending "+pending);

                        int updatePendingByRent = entityManager.createNativeQuery("UPDATE rent.dbo.pending SET " +
                                "pending = 0 WHERE tid = :tid").setParameter("tid", tid).executeUpdate();
                        int update_pending = entityManager.createNativeQuery(query)
                                .setParameter("tid", tid)
                                .setParameter("date", date)
                                .setParameter("collected", collected).executeUpdate();
                    } else {
                        System.out.println("lower");
                        pending = pending - txnAmount;
                        collected = txnAmount;
                        System.out.println("collected "+collected);
                        System.out.println("pending "+pending);
                        int updatePendingByRent = entityManager.createNativeQuery("UPDATE rent.dbo.pending SET " +
                                "pending = :pending WHERE tid = :tid").setParameter("tid", tid)
                                .setParameter("pending", pending).executeUpdate();
                        int update_pending = entityManager.createNativeQuery(query).
                                setParameter("tid", tid).
                                setParameter("date", date).
                                setParameter("collected", collected).executeUpdate();
                    }
                });
            });
        });
        try {
            entityManager.createNativeQuery("UPDATE   m SET m.collected  = f.collected FROM rent.dbo.pending m INNER JOIN " +
                    "( SELECT tid ,SUM(collected) collected FROM  rent.dbo.collection_details group by tid )f ON m.TID = f.tid;").executeUpdate();
            entityManager.createNativeQuery("UPDATE rent.dbo.pending SET  collected =0 where collected is null;").executeUpdate();
            entityManager.createNativeQuery("truncate table rent.dbo.pending_hist;" +
                    "INSERT INTO rent.dbo.pending_hist SELECT * FROM rent.dbo.pending; ").executeUpdate();
        }catch (Exception e){
            System.out.println("Error while update hist");
            e.printStackTrace();
        }
    }

    @Transactional
    @Modifying
    @Override
    public void importData(boolean action) {
        if (action) {
            int delete = entityManager.createNativeQuery("DELETE TABLE rent.dbo.pending").executeUpdate();
            System.out.println("delete pending records " + delete);
        }
        String fileName = Constants.UPLOAD_DOCUMENT_DIR_MIS +Constants.PENDING_XLSX;
        Workbook wb = null;
        Sheet sh = null;
        try {
             wb = new XSSFWorkbook(fileName);
             sh = wb.getSheetAt(0);
            boolean flag = true;
            for (Row rw : sh) {
                if (flag) {
                    flag = false;
                    continue;
                }
                int count = 0;
                String newTid = rw.getCell(count++).toString();
                double newRent = rw.getCell(count++).getNumericCellValue();
                double newPending = rw.getCell(count++).getNumericCellValue();
                ;
                String tidAlreadyexists = "SELECT * FROM rent.dbo.pending where tid = :tid";
                List<Object[]> exists = entityManager.createNativeQuery(tidAlreadyexists)
                        .setParameter("tid", newTid).getResultList();
                if (!(exists.size() > 0)) {
                    entityManager.createNativeQuery("INSERT INTO rent.dbo.pending(tid,rent,pending) values" +
                            " (:tid,:rent,:pending)")
                            .setParameter("tid", newTid)
                            .setParameter("rent", newRent)
                            .setParameter("pending", newPending)
                            .executeUpdate();
                } else {
                    exists.forEach(l -> {
                        double updatedOldPending = Float.valueOf(l[2].toString());
                        double updatedpending = Float.valueOf(l[2].toString()) + newPending;
                        System.out.println("updatedOldPending " + updatedOldPending);
                        System.out.println("updatedpending" + updatedpending);

                        entityManager.createNativeQuery("UPDATE rent.dbo.pending SET oldpending =:updatedOldPending" +
                                ",pending =:updatedPending WHERE tid = :tid")
                                .setParameter("tid", newTid)
                                .setParameter("updatedOldPending", updatedOldPending)
                                .setParameter("updatedPending", updatedpending)
                                .executeUpdate();
                    });
                }
            }
            wb.close();
        } catch (Exception e) {
              throw new FileStorageException("could not import data into database or please try again",e);
        }
    }

}
