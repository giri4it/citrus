package com.consol.citrus.ws.config.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CitrusWSConfigNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("jetty-server", new JettyServerParser());
        registerBeanDefinitionParser("message-sender", new WebServiceMessageSenderParser());
        registerBeanDefinitionParser("reply-message-handler", new WebServiceReplyMessageReceiverParser());
    }

}