package org.cloud.scheduler.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调度器失败恢复时misfire规则枚举
 */
public class MisfireEnum {

    public static final String CRON_SCHEDULE_MISFIRE_MODE_KEY = "CRON_SCHEDULE_MISFIRE_MODE";
    public static final String SIMPLE_SCHEDULE_MISFIRE_MODE_KEY = "SIMPLE_SCHEDULE_MISFIRE_MODE";

    /**
     * cron类型的调度器Misfire模式枚举
     */
    @Getter
    @AllArgsConstructor
    public static enum CronScheduleMisfireEnum {

        /**
         * ——以当前时间为触发频率立刻触发一次执行
         * ——然后按照Cron频率依次执行
         */
        MISFIRE_INSTRUCTION_FIRE_ONCE_NOW(1, "withMisfireHandlingInstructionFireAndProceed", ""),

        /**
         ——不触发立即执行
         ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
         */
        MISFIRE_INSTRUCTION_DO_NOTHING(2, "withMisfireHandlingInstructionDoNothing", ""),

        /**
         *
         ——以错过的第一个频率时间立刻开始执行
         ——重做错过的所有频率周期后
         ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
         即: 忽略所有的超时状态，按照触发器的策略执行。
         */
        MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY(-1, "withMisfireHandlingInstructionIgnoreMisfires", "")
        ;
        private int misfireValue;

        //设置misfire模式的函数名称，不能出现错误，需要使用反射调用指定名字的函数
        private String misfireMethodName;

        private String misfireDesc;

    }

    /**
     * Simple类型的调度器Misfire模式枚举
     */
    @Getter
    @AllArgsConstructor
    public static enum SimpleScheduleMisfireEnum {

        /**
         *
         ——以当前时间为触发频率立即触发执行
         ——执行至FinalTIme的剩余周期次数
         ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
         ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
         */
        MISFIRE_INSTRUCTION_NOW_WITH_EXISTING_COUNT(2, "withMisfireHandlingInstructionNowWithExistingCount", ""),

        /**
         *
         ——以当前时间为触发频率立即触发执行
         ——执行至FinalTIme的剩余周期次数
         ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
         ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
         */
        MISFIRE_INSTRUCTION_FIRE_NOW(1, "withMisfireHandlingInstructionFireNow", ""),

        /**
         *
         ——以错过的第一个频率时间立刻开始执行
         ——重做错过的所有频率周期
         ——当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
         ——共执行RepeatCount+1次
         */
        MISFIRE_INSTRUCTION_IGNORE_MISFIRES(-1, "withMisfireHandlingInstructionIgnoreMisfires", ""),

        /**
         *
         ——不触发立即执行
         ——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
         ——以startTime为基准计算周期频率，并得到FinalTime
         ——即使中间出现pause，resume以后保持FinalTime时间不变
         */
        MISFIRE_INSTRUCTION_NEXT_WITH_EXISTING_COUNT(5, "withMisfireHandlingInstructionNextWithExistingCount", ""),

        /**
         *
         ——不触发立即执行 （失败不重试建议使用此模式）
         ——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
         ——以startTime为基准计算周期频率，并得到FinalTime
         ——即使中间出现pause，resume以后保持FinalTime时间不变
         */
        MISFIRE_INSTRUCTION_NEXT_WITH_REMAINING_COUNT(4, "withMisfireHandlingInstructionNextWithRemainingCount", ""),

        /**
         *
         ——以当前时间为触发频率立即触发执行
         ——执行至FinalTIme的剩余周期次数
         ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
         ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值
         */
        MISFIRE_INSTRUCTION_NOW_WITH_REMAINING_COUNT(3, "withMisfireHandlingInstructionNowWithRemainingCount", ""),


        /**
         * MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
         * ——此指令导致trigger忘记原始设置的starttime和repeat-count
         * ——触发器的repeat-count将被设置为剩余的次数
         * ——这样会导致后面无法获得原始设定的starttime和repeat-count值
         * ————————————————
         */

        ;
        private int misfireValue;

        //设置misfire模式的函数名称，不能出现错误，需要使用反射调用指定名字的函数
        private String misfireMethodName;

        private String misfireDesc;
    }
}
