`POST /api/v2/admin/users/{userId}/revokeAllTokens`

Invalidate all previously issued authentication tokens for the user including active browser sessions and long-lived
development tokens. If the user has active sessions in a browser, this will force re-authentication.

Previously issued authentication tokens may not appear as explicitly revoked but they will not be considered valid when
used to authenticate requests. The invalidation may not take effect immediately, but will take effect within a couple of minutes.

The caller must have permission to manage users for the target user's organization.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `userId` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `UserDeleted` | The user is deleted. |
| PERMISSION_DENIED | `RevokeAllTokensUserPermissionDenied` | Could not revokeAllTokens the User. |
| NOT_FOUND | `UserNotFound` | The given User could not be found. |
