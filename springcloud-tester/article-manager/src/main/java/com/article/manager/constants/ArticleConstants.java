package com.article.manager.constants;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ArticleConstants {

    /**
     * 通用的状态枚举
     */
    public static enum StatusEnum {
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 禁止
         */
        FORBIDDEN(0),
        ;
        private int status;

        StatusEnum(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    /**
     * 文章类型枚举
     */
    public static enum ArticleNodeTypeEnum {

        /**
         * 文章分类
         */
        CLASS(1),

        /**
         * 文章
         */
        ARTICLE(2),
        ;

        private int type;

        ArticleNodeTypeEnum(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 语言类型枚举
     */
    @Getter
    @AllArgsConstructor
    public static enum LanguageTypeEnum {

        ZH(1, "中文"),

        EN(2, "英文"),
        ;

        private int type;

        private String desc;
    }

    /**
     * 文章分类编码枚举
     */
    public static enum ArticleClassCodeEnum {
        /**
         * 公告
         */
        NOTICE("NOTICE"),

        /**
         * 项目专场
         */
        PROJECT("PROJECT"),

        /**
         * 新手指南
         */
        HELP_FAQ("HELP_FAQ"),
        ;
        private String classCode;

        ArticleClassCodeEnum(String classCode) {
            this.classCode = classCode;
        }

        public String getClassCode() {
            return classCode;
        }

        private final static Map<String, ArticleClassCodeEnum> map = new HashMap<String, ArticleClassCodeEnum>();

        static {
            map.put(NOTICE.getClassCode(), NOTICE);
            map.put(PROJECT.getClassCode(), PROJECT);
            map.put(HELP_FAQ.getClassCode(), HELP_FAQ);
        }

        public static ArticleClassCodeEnum getByCode(String code) {
            return map.get(code);
        }
    }
}
