package com.pedometerlibrary.widget;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/14 15:39
 * <p>
 * NotifyThme
 */
public enum NotifyThme {
    EMPTY(),
    SIMPLE(),
    MINUTE();



    public static NotifyThme matched(int theme) {
        if (theme == 0) {
            return SIMPLE;
        }
        if (theme == 1) {
            return MINUTE;
        }
        return SIMPLE;
    }

    public static int matched(NotifyThme theme) {
        if (theme == NotifyThme.SIMPLE) {
            return 0;
        }
        if (theme == NotifyThme.MINUTE) {
            return 1;
        }
        return 0;
    }

}
