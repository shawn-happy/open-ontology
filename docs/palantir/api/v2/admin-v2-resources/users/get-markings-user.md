`GET /api/v2/admin/users/{userId}/getMarkings`

Retrieve Markings that the user is currently a member of.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `userId` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |

## Response

**GetUserMarkingsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetUserMarkingsResponse` | object | 是 | 示例: `{"view":["18212f9a-0e63-4b79-96a0-aae04df23336"]}` |
| `GetUserMarkingsResponse.view` | list<MarkingId> | 否 | The markings that the user has access to. The user will be able to access resources protected with these<br>markings. This includes organization markings for organizations in which the user is a guest member. |
| `GetUserMarkingsResponse.view.MarkingId` | string | 是 | The ID of a security marking. |

```json
{
  "view": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `UserDeleted` | The user is deleted. |
| PERMISSION_DENIED | `GetMarkingsUserPermissionDenied` | Could not getMarkings the User. |
| NOT_FOUND | `UserNotFound` | The given User could not be found. |
