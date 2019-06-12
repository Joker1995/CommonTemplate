package com.tisson.demo.common.expt;

public class RegisterUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RegisterUserException(String msg) {
        super(msg);
    }

    public RegisterUserException() {
        super();
    }
}
