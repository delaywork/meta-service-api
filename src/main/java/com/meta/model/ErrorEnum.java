package com.meta.model;

public enum ErrorEnum {

    //-----------------------------------------------------------------------------------------------------------------
    // 客户端异常，“ C ”开头
    //-----------------------------------------------------------------------------------------------------------------
    网络异常("NETWORK_EXCEPTION", "Network anomaly"),
    Token过期或已失效("TOKEN_EXPIRED", "The token has expired or become invalid"),
    没有文件操作权限("DOCUMENT_PERMISSION_EXCEPTION", "没有文件操作权限"),
    文件类型错误("DOCUMENT_TYPE_EXCEPTION", "文件类型错误"),
    文件已删除("DOCUMENT_DELETED_EXCEPTION", "文件已删除"),
    文件不存在("DOCUMENT_NON_EXISTENT_EXCEPTION", "文件不存在"),
    文件不存在有效地址("DOCUMENT_ADDRESS_NON_EXISTENT_EXCEPTION", "文件不存在有效地址"),
    下载失败("DOWNLOAD_EXCEPTION", "下载失败"),
    登录失败("LOGIN_EXCEPTION", "登录失败"),
    code无效("CODE_EXCEPTION", "code无效"),
    登录频率限制("LOGIN_RATE_EXCEPTION", "登录频率限制"),
    高风险用户("HIGH_RISK_ACCOUNT", "高风险用户"),
    添加水印失败("WATERMARK_EXCEPTION", "添加水印失败"),
    父级文件夹不存在("DOCUMENT_PARENT_EXCEPTION", "父级文件夹不存在"),
    根目录不能被删除("ROOT_CANNOT_DELETE", "根目录不能被删除"),
    不能使用根目录命名("CANNOT_USE_ROOT_NAMING", "不能使用根目录命名"),
    该分享已被禁止其他用户访问("OPPORTUNITY_CANNOT_ACCESS", "该分享已被禁止其他用户访问"),
    该分享已被禁止其他用户分享("OPPORTUNITY_CANNOT_SHARE", "该分享已被禁止其他用户分享"),
    验证码验证失败("CODE_VERIFY_EXCEPTION", "验证码验证失败"),
    验证码发送失败("CODE_SEND_EXCEPTION", "验证码发送失败"),
    该手机号已被绑定其他账号("PHONE_BOUND", "该手机号已被绑定其他账号"),
    帐户未验证("ACCOUNT_NOT_VERIFIED", "帐户未验证"),
    帐户不存在("ACCOUNT_NON_EXISTENT", "帐户不存在"),
    不能移动文件("CANNOT_MOVE_EXCEPTION", "不能移动文件"),
    //-----------------------------------------------------------------------------------------------------------------
    // 服务端异常，“ B ”开头
    //-----------------------------------------------------------------------------------------------------------------
    参数不正确("PARAMS_EXCEPTION", "参数不正确"),
    认证异常("AUTH_EXCEPTION", "认证异常"),
    token异常("TOKEN_EXCEPTION", "token异常"),
    权限异常("ACCESS_DENIED_EXCEPTION", "权限异常"),
    微信白名单异常("WECHAT_WHITE_LIST_EXCEPTION", "微信白名单异常"),
    发送验证码频率异常("SEND_VERIFICATION_CODE_FREQUENCY_EXCEPTION", "发送验证码频率异常"),

    //-----------------------------------------------------------------------------------------------------------------
    // 其它异常（第三方服务调用失败），“ O ”开头
    //-----------------------------------------------------------------------------------------------------------------
    第三方调用失败("THIRD_EXCEPTION", "Third party request failed"),
    ;

    public final String code;

    public final String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String messageOf(String message) {
        for (ErrorEnum e : ErrorEnum.values()) {
            if (e.message.equalsIgnoreCase(message)) {
                return e.code;
            }
        }
        return "SUCCESS";
    }
}
