`DELETE /api/v2/admin/users/{userId}`

Delete the User with the specified id.

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
| PERMISSION_DENIED | `DeleteUserPermissionDenied` | Could not delete the User. |
| NOT_FOUND | `UserNotFound` | The given User could not be found. |
