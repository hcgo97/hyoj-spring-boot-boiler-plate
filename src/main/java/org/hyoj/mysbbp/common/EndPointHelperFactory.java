package org.hyoj.mysbbp.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EndPointHelperFactory {

    @Autowired
    private ApplicationContext appContext;

    public EndPointHelper factory() {
        return appContext.getBean("endPointHelper", EndPointHelper.class);
    }
}
