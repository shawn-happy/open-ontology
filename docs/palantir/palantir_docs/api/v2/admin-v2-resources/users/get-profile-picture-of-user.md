`GET /api/v2/admin/users/{userId}/profilePicture`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `userId` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |

## Response

**body**

The user's profile picture in binary format. The format is the original format uploaded by the user.<br>The response will contain a `Content-Type` header that can be used to identify the media type.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | The user's profile picture in binary format. The format is the original format uploaded by the user.<br>The response will contain a `Content-Type` header that can be used to identify the media type. |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidProfilePicture` | The user's profile picture is not a valid image |
| INTERNAL | `ProfileServiceNotPresent` | The Profile service is unexpectedly not present. |
| INVALID_ARGUMENT | `UserDeleted` | The user is deleted. |
| PERMISSION_DENIED | `GetProfilePictureOfUserPermissionDenied` | Could not profilePicture the User. |
| NOT_FOUND | `UserNotFound` | The given User could not be found. |
