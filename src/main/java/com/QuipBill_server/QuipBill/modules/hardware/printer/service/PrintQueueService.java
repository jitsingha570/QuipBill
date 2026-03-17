package com.QuipBill_server.QuipBill.modules.hardware.printer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Slf4j
public class PrintQueueService {

    private final Queue<Runnable> queue = new LinkedList<>();

    public synchronized void addJob(Runnable job) {

        queue.add(job);

        processQueue();
    }

    private void processQueue() {

        while (!queue.isEmpty()) {

            Runnable job = queue.poll();

            try {

                job.run();

            } catch (Exception e) {

                log.error("Print job failed", e);
            }
        }
    }
}