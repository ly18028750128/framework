package com.longyou.paycenter.constant;

public class WeixinPayConstants {


    /* 统一下单
    * 应用场景
        商户在小程序中先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易后调起支付。
    接口链接
        URL地址：https://api.mch.weixin.qq.com/pay/unifiedorder
    */
    public static enum ColumnUnifiedorder {
        APPID("小程序ID","appid","String(32)|是|微信分配的小程序ID"),
        MCH_ID("商户号","mch_id","String(32)|是|微信支付分配的商户号"),
        DEVICE_INFO("设备号","device_info","String(32)|否|自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传\"WEB\""),
        NONCE_STR("随机字符串","nonce_str","String(32)|是|随机字符串，长度要求在32位以内。推荐随机数生成算法"),
        SIGN("签名","sign","String(64)|是|通过签名算法计算得出的签名值，详见签名生成算法"),
        SIGN_TYPE("签名类型","sign_type","String(32)|否|签名类型，默认为MD5，支持HMAC-SHA256和MD5。"),
        BODY("商品描述","body","String(128)|是|商品简单描述，该字段请按照规范传递，具体请见参数规定"),
        DETAIL("商品详情","detail","String(6000)|否|商品详细描述，对于使用单品优惠的商户，该字段必须按照规范上传，详见“单品优惠参数说明”"),
        ATTACH("附加数据","attach","String(127)|否|附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。"),
        OUT_TRADE_NO("商户订单号","out_trade_no","String(32)|是|商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号"),
        FEE_TYPE("标价币种","fee_type","String(16)|否|符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型"),
        TOTAL_FEE("标价金额","total_fee","Int|是|订单总金额，单位为分，详见支付金额"),
        SPBILL_CREATE_IP("终端IP","spbill_create_ip","String(64)|是|支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP"),
        TIME_START("交易起始时间","time_start","String(14)|否|订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则"),
        TIME_EXPIRE("交易结束时间","time_expire","String(14)|否|订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则,建议：最短失效时间间隔大于1分钟"),
        GOODS_TAG("订单优惠标记","goods_tag","String(32)|否|订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠"),
        NOTIFY_URL("通知地址","notify_url","String(256)|是|异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。"),
        TRADE_TYPE("交易类型","trade_type","String(16)|是|小程序取值如下：JSAPI，详细说明见参数规定"),
        PRODUCT_ID("商品ID","product_id","String(32)|否|trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。"),
        LIMIT_PAY("指定支付方式","limit_pay","String(32)|否|上传此参数no_credit--可限制用户不能使用信用卡支付"),
        OPENID("用户标识","openid","String(128)|否|trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。"),
        RECEIPT("电子发票入口开放标识","receipt","String(8)|否|Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效"),
        SCENE_INFO("-场景信息","scene_info","String(256)|否|该字段常用于线下活动时的场景信息上报，支持上报实际门店信息，商户也可以按需求自己上报相关信息。该字段为JSON对象数据，对象格式为{\"store_info\":{\"id\": \"门店ID\",\"name\": \"名称\",\"area_code\": \"编码\",\"address\": \"地址\" }} ，字段详细说明请点击行前的+展开"),
        ID("-门店id","id","String(32)|否|门店编号，由商户自定义"),
        NAME("-门店名称","name","String(64)|否|门店名称 ，由商户自定义"),
        AREA_CODE("-门店行政区划码","area_code","String(6)|否|门店所在地行政区划码，详细见《最新县及县以上行政区划代码》"),
        ADDRESS("-门店详细地址","address","String(128)|否|门店详细地址 ，由商户自定义"),
        ;
        private String name;
        private String columnName;
        private String description;

        ColumnUnifiedorder(String name,String columnName, String description) {
            this.name = name;
            this.columnName = columnName;
            this.description = description;
        }

        public String value() {
            return this.columnName;
        }
    }

    public static enum ColumnRefund {
        APPID("小程序ID","appid","String(32)微信分配的小程序ID"),
        MCH_ID("商户号","mch_id","String(32)微信支付分配的商户号"),
        NONCE_STR("随机字符串","nonce_str","String(32)随机字符串，不长于32位。推荐随机数生成算法"),
        SIGN("签名","sign","String(32)签名，详见签名生成算法"),
        SIGN_TYPE("签名类型","sign_type","String(32)签名类型，目前支持HMAC-SHA256和MD5，默认为MD5"),
        TRANSACTION_ID("微信订单号","transaction_id","String(32)微信生成的订单号，在支付通知中有返回"),
        OUT_TRADE_NO("商户订单号","out_trade_no","String(32)商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。transaction_id、out_trade_no二选一，如果同时存在优先级：transaction_id> out_trade_no"),
        OUT_REFUND_NO("商户退款单号","out_refund_no","String(64)商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。"),
        TOTAL_FEE("订单金额","total_fee","Int订单总金额，单位为分，只能为整数，详见支付金额"),
        REFUND_FEE("退款金额","refund_fee","Int退款总金额，订单总金额，单位为分，只能为整数，详见支付金额"),
        REFUND_FEE_TYPE("货币种类","refund_fee_type","String(8)货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"),
        REFUND_DESC("退款原因","refund_desc","String(80)若商户传入，会在下发给用户的退款消息中体现退款原因注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因"),
        REFUND_ACCOUNT("退款资金来源","refund_account","String(30)仅针对老资金流商户使用REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）"),
        NOTIFY_URL("退款结果通知url","notify_url","String(256)异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数"),
        ;
        private String name;
        private String columnName;
        private String description;

        ColumnRefund(String name,String columnName, String description) {
            this.name = name;
            this.columnName = columnName;
            this.description = description;
        }

        public String value() {
            return this.columnName;
        }

    }

}
