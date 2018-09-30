package com.example.demo.filter;

import com.example.demo.utils.DateUtil;
import com.example.demo.utils.MD5Util;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/30 上午10:19
 */
@Component
public class ValidateSignFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ValidateSignFilter.class);
    @Value("#{${secret.map}}")
    private Map<String, String> secretMap;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String sign = request.getHeader("sign");
        Boolean boo = validateSign(sign);
        if (boo) {
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);
        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(400);
            ctx.setResponseBody("sign validate fail");
            ctx.set("isSuccess", false);
        }
        return null;
    }

    private Boolean validateSign(String sign) {
        if (StringUtils.isEmpty(sign)) {
            logger.error("sign为空");
            return false;
        }
        String[] splitSign = sign.split("&");
        if (splitSign.length < 2) {
            logger.error("sign格式不正确");
            return false;
        }
        if (secretMap.containsKey(splitSign[0])) {
            String secret = secretMap.get(splitSign[0]);
            String date = DateUtil.getDate();
            StringBuffer sb = new StringBuffer();
            sb.append(secret).append('&').append(date);
            String encryptMD5 = MD5Util.encryptMD5(sb.toString());
            if (splitSign[1].equals(encryptMD5)) {
                return true;
            } else {
                logger.error("sign验证不通过");
                return false;
            }
        } else {
            logger.error("id不存在");
            return false;
        }
    }
}
