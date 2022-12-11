package com.unknow.first.mail.manager;

import java.io.Serializable;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class JavaMailSenderSerializable extends JavaMailSenderImpl implements Serializable {

    private static final long serialVersionUID = 1412048124194324197L;

//    public JavaMailSenderSerializable() {
//        super();
//    }

}
