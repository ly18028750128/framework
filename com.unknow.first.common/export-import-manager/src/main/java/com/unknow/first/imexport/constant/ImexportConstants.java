package com.unknow.first.imexport.constant;

import lombok.AllArgsConstructor;

public interface ImexportConstants {

    @AllArgsConstructor
    enum ProcessStatus {
        //        任务状态：1(未执行) 2(执行中)3(执行成功)-1(执行失败)
        no_process(1, "未执行"),
        processing(2, "执行中"),
        success(3, "执行成功"),
        fail(-1, "执行失败"),
        ;

        public final int value;
        public final String name;

    }

}
