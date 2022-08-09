package com.hai.micro.common.other.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName BaseTagEnum
 * @Description: 消息tag统一定义
 * @Author zxh
 * @Date 2022/08/09 11:40
 * @Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum BaseTagEnum {

    /**
     * 物联网IOT消息
     */
    IOT,

    /**
     * 开门视频切分完成
     */
    EVENT_VIDEO_SPLIT,

    /**
     * 用户关门通知
     */
    USER_CLOSE_DOOR,

    /**
     * 新增客服用户时同步给工单应用
     */
    CUSTOMER_USER_SYNC,

    /**
     * 客服用户登录、退出时同步在离线状态给工单应用
     */
    CUSTOMER_WORK_STATE_SYNC,

    /**
     * 推送客服工单识别结果
     */
    SHEET_RECOGNIZE_RESULT,

    /**
     * 转客服处理的识别订单推送
     */
    SHEET_RECOGNIZE_ORDER,

    /**
     * 租户商品审核结果回调商户
     */
    TENANT_REVIEW_RESULT,

    /**
     * 摄像头故障上报
     */
    MACHINE_MONITOR_REPORT,

    /**
     * 商户回调通知
     */
    NOTIFY_CALL;
}
