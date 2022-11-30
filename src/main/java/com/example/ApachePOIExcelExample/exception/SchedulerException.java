package com.example.ApachePOIExcelExample.exception;

public class SchedulerException extends RuntimeException {

    private static final long serialVersionUID = 7983747651240802398L;

    public static final String JOB_NOT_FOUND = "Scheduled Job с идентификатором '%s' не найден";
    public static final String JOB_LOGGER_NOT_FOUND = "Scheduled Job Logger с идентификатором '%s' не найден";
    public static final String JOB_START_ERROR = "Ошибка запуска фонового задания";
    public static final String JOB_PARAMETERS_WRONG_FORMAT = "Заданы некорректные параметры для Scheduled Job";
    public static final String INVALID_CRON_EXPRESSION = "Некорректный формат cron выражения";
    public static final String INVALID_JOB_PARAMETERS = "Некорректный формат Scheduled Job параметров";

    public SchedulerException(String msg) {
        super(msg);
    }

}