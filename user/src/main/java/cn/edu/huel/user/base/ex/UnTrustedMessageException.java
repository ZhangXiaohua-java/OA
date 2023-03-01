package cn.edu.huel.user.base.ex;

/**
 * @author 张晓华
 * @date 2022-12-14
 */
public class UnTrustedMessageException extends RuntimeException {


	public UnTrustedMessageException(Object source) {
		super(source.toString());
	}


}
