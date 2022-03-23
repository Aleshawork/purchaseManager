package com.manager.purchaseManager.jobs;

import com.manager.purchaseManager.mail.MailManager;
import com.manager.purchaseManager.model.Item;
import com.manager.purchaseManager.model.Purchase;
import com.manager.purchaseManager.service.PurchaseService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Component
public class PurchaseTasklet implements Tasklet
{
    private final MailManager mailManager;
    private final PurchaseService purchaseService;
    //todo: вынести отдельно
    private final static Date defaultLastDate = Date.from(LocalDate.of(2019, Month.SEPTEMBER,1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    public PurchaseTasklet(MailManager mailManager, PurchaseService purchaseService) {
        this.mailManager = mailManager;
        this.purchaseService = purchaseService;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Date lastDate =null;

            Item lastItem = purchaseService.findItemWithLastDate();
            if (lastItem ==null){
                lastDate =defaultLastDate;
            }
            else {
                lastDate = lastItem.getDate_of_buy();
            }

        ArrayList<Purchase> purchases = mailManager.readPurchase(lastDate);
        if (purchases.size()>0){
           purchaseService.savePurchaseList(purchases);
        }

        return RepeatStatus.FINISHED;
    }
}
