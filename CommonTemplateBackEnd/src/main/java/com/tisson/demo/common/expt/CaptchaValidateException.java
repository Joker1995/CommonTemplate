package com.tisson.demo.common.expt;

public class CaptchaValidateException  extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CaptchaValidateException(String msg) {
        super(msg);
    }

    public CaptchaValidateException() {
        super();
    }
}
