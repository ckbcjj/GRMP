package com.valor.model.exception;

public class UpmsHttpCode {

	public static final int OK = 0;

	public static final int WEB_ERROR = 566; // Web error

	public static final int SERVER_ERROR = 555; // Api error
	public static final String SERVER_ERROR_MSG = "Server Internal Error";

	public static final int RET_QUERY = 701; //查询错误
	public static final int RET_UPDATE = 702; //更新错误

	public static final int ERR_INVALID_ARGS = 1000;// 无效参数
	public static final String ERR_INVALID_ARGS_MSG = "Invalid Args";

	public static final int ERR_MISS_PARAM_AUTH = 1001;
	public static final String ERR_MISS_PARAM_AUTH_MSG = "miss param authority";

	public static final int ERR_MISS_PARAM_USERSTATE = 1002;
	public static final String ERR_MISS_PARAM_USERSTATE_MSG = "miss param state";

	public static final int ERR_PARAM_SEARCHVALUE_INVALID = 1003;
	public static final String ERR_PARAM_SEARCHVALUE_INVALID_MSG = "param searchvalue invalid";

	public static final int ERR_MISS_PARAM_ROLE = 1004;
	public static final String ERR_MISS_PARAM_ROLE_MSG = "miss param role";

	public static final int ERR_MISS_PARAM_USER = 1005;
	public static final String ERR_MISS_PARAM_USER_MSG = "miss param user";

	public static final int ERR_MISS_PARAM_SOURCE = 1006;
	public static final String ERR_MISS_PARAM_SOURCE_MSG = "miss param source";


	public static final int ERR_INVALID_OP = 2000;// 非法操作
	public static final String ERR_INVALID_OP_MSG = "Bad Operation";

	public static final int ERR_ROLE_NOT_EXIST = 2001;
	public static final String ERR_ROLE_NOT_EXIST_MSG = "the role is not exist";

	public static final int ERR_USER_EXIST = 2002;
	public static final String ERR_USER_EXIST_MSG = "the user is already exist";

	public static final int ERR_USER_NOT_EXIST = 2003;
	public static final String ERR_USER_NOT_EXIST_MSG = "the user is not exist";

	public static final int ERR_AUTH_NOT_EXIST = 2004;
	public static final String ERR_AUTH_NOT_EXIST_MSG = "the authority is not exist";

	public static final int ERR_HAS_CHILD_NODES = 2005;
	public static final String ERR_HAS_CHILD_NODES_MSG = "please delete child nodes first";

	public static final int ERR_SOURCE_NOT_EXIST = 2006;
	public static final String ERR_SOURCE_NOT_EXIST_MSG = "the source is not exist";

	public static final int ERR_SOURCE_EXIST = 2007;
	public static final String ERR_SOURCE_EXIST_MSG = "the source is already exist";

	public static final int ERR_SOURCE_UNAVALIABLE = 2008;
	public static final String ERR_SOURCE_UNAVALIABLE_MSG = "the source is unavaliable";

	public static final int ERR_SUPER_ROLE_DELETE = 2009;
	public static final String ERR_SUPER_ROLE_DELETE_MSG = "super role can not be deleted";

	public static final int ERR_SUPER_USER_DELETE = 2010;
	public static final String ERR_SUPER_USER_DELETE_MSG = "super user can not be deleted";

}
