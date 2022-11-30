package com.example.ApachePOIExcelExample.scheduler.job;

import java.io.IOException;

public interface ScheduledJob extends Runnable {

    String parseParameters(String parameters) throws IOException;

}