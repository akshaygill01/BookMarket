package com.akshay.book.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
//enum
    ACTIVATE_ACCOUNT("activate_account");
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }

}
