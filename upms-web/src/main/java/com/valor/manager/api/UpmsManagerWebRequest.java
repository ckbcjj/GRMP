package com.valor.manager.api;

import com.valor.model.web.request.*;
import com.valor.model.web.response.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UpmsManagerWebRequest {

    @POST("/api/upms/sysAuthority/query/v1")
    Call<UpmsResponse<PageResultResponse<SysAuthorityResponse>>> authQuery(@Body SysAuthorityRequest args);

    @POST("/api/upms/sysAuthority/getAuths/v1")
    Call<UpmsResponse<PageResultResponse<SysAuthorityResponse>>> getAuths(@Body SysRoleAuthRequest args);

    @POST("/api/upms/sysAuthority/update/v1")
    Call<UpmsResponse<Boolean>> authUpdate(@Body SysAuthorityRequest args);

    @POST("/api/upms/sysAuthority/delete/v1")
    Call<UpmsResponse<Boolean>> authDelete(@Body SysAuthorityRequest args);

    @POST("/api/upms/source/query/v1")
    Call<UpmsResponse<PageResultResponse<SysSourceResponse>>> sourceQuery(@Body SysSourceRequest args);

    @POST("/api/upms/source/update/v1")
    Call<UpmsResponse<Boolean>> sourceUpdate(@Body SysSourceRequest args);

    @POST("/api/upms/log/query/v1")
    Call<UpmsResponse<PageResultResponse<SysLogResponse>>> logQuery(@Body SysLogRequest args);

    @POST("/api/upms/role/query/v1")
    Call<UpmsResponse<PageResultResponse<SysRoleResponse>>> roleQuery(@Body SysRoleRequest args);

    @POST("/api/upms/role/update/v1")
    Call<UpmsResponse<Boolean>> roleUpdate(@Body SysRoleRequest args);

    @POST("/api/upms/role/getRoles/v1")
    Call<UpmsResponse<PageResultResponse<SysRoleResponse>>> getRoles(@Body SysUserRoleRequest args);

    @POST("/api/upms/role/updateAuths/v1")
    Call<UpmsResponse<Boolean>> updateAuths(@Body SysRoleAuthRequest args);

    @POST("/api/upms/role/delete/v1")
    Call<UpmsResponse<Boolean>> roleDelete(@Body SysRoleRequest args);

    @POST("/api/upms/user/query/v1")
    Call<UpmsResponse<PageResultResponse<SysUserResponse>>> userQuery(@Body SysUserRequest args);

    @POST("/api/upms/user/update/v1")
    Call<UpmsResponse<Boolean>> userUpdate(@Body SysUserRequest args);

    @POST("/api/upms/user/updateRoles/v1")
    Call<UpmsResponse<Boolean>> updateRoles(@Body SysUserRoleRequest args);

    @POST("/api/upms/user/delete/v1")
    Call<UpmsResponse<Boolean>> userDelete(@Body SysUserRequest args);
}
