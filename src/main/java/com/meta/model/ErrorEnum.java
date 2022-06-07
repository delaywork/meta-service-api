package com.meta.model;

public enum ErrorEnum {

    //-----------------------------------------------------------------------------------------------------------------
    // 客户端异常，“ C ”开头
    //-----------------------------------------------------------------------------------------------------------------
    网络异常("C-00001", "Network anomaly"),
    Token过期或已失效("C-00002", "The token has expired or become invalid"),
    没有文件操作权限("C-00003", "没有文件操作权限"),
    不是文件夹类型("C-00004", "不是文件夹类型"),
    文件已删除("C-00005", "文件已删除"),
    文件不存在("C-00006", "文件不存在"),
    文件不存在有效地址("C-00007", "文件不存在有效地址"),
    下载失败("C-00008", "下载失败"),
    登录失败("C-00009", "登录失败"),
    code无效("C-00010", "code无效"),
    登录频率限制("C-00011", "登录频率限制"),
    高风险用户("C-00012", "高风险用户"),
    添加水印失败("C-00013", "添加水印失败"),
    原有父级文件夹不存在("C-00014", "原有父级文件夹不存在"),
    根目录不能被删除("C-00015", "根目录不能被删除"),
    不能使用根目录命名("C-00016", "不能使用根目录命名"),
    该分享已被禁止其他用户访问("C-00017", "该分享已被禁止其他用户访问"),
    该分享已被禁止其他用户分享("C-00018", "该分享已被禁止其他用户分享"),
    验证码验证失败("C-00019", "验证码验证失败"),
    验证码发送失败("C-00020", "验证码发送失败"),
    该手机号已被绑定其他账号("C-00020", "该手机号已被绑定其他账号"),
    帐户未验证("C-00021", "帐户未验证"),
    帐户不存在("C-00022", "帐户不存在"),
    不能移动到子文件夹("C-00023", "不能移动到子文件夹"),
    //-----------------------------------------------------------------------------------------------------------------
    // 服务端异常，“ B ”开头
    //-----------------------------------------------------------------------------------------------------------------
    参数不正确("B-00001", "Parameter error"),
    认证异常("AUTH_EXCEPTION", "auth exception"),
    token异常("TOKEN_EXCEPTION", "token exception"),
    权限异常("ACCESS_DENIED_EXCEPTION", "access denied exception"),
    微信白名单异常("WECHAT_WHITE_LIST_EXCEPTION", "wechat white list exception"),
    //-----------------------------------------------------------------------------------------------------------------
    // 其它异常（第三方服务调用失败），“ O ”开头
    //-----------------------------------------------------------------------------------------------------------------
    第三方调用失败("O-00001", "Third party request failed"),
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
        return "B-00000";
    }
}
