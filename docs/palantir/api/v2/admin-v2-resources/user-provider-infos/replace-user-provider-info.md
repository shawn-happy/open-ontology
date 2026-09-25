`PUT /api/v2/admin/users/{userId}/providerInfo`

Replace the UserProviderInfo.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `userId` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |

## Request body

```json
{
  "providerId": "2838c8f3-d76a-4e99-acf1-1dee537e4c48"
}
```

## Response

**UserProviderInfo**

The replaced UserProviderInfo

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `UserProviderInfo` | object | 是 | The replaced UserProviderInfo<br>示例: `{"providerId":"2838c8f3-d76a-4e99-acf1-1dee537e4c48"}` |
| `UserProviderInfo.providerId` | string | 是 | The ID of the User in the external authentication provider. This value is determined by the authentication provider.<br>At most one User can have a given provider ID in a given Realm.<br>示例: `2838c8f3-d76a-4e99-acf1-1dee537e4c48` |

```json
{
  "providerId": "2838c8f3-d76a-4e99-acf1-1dee537e4c48"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetUserProviderInfoPermissionDenied` | The provided token does not have permission to view the provider information for the given user. |
| INVALID_ARGUMENT | `CannotReplaceProviderInfoForPrincipalInProtectedRealm` | Provider information for Principals in this Realm cannot be replaced. |
| INVALID_ARGUMENT | `UserDeleted` | The user is deleted. |
| PERMISSION_DENIED | `ReplaceUserProviderInfoPermissionDenied` | Could not replace the UserProviderInfo. |
| NOT_FOUND | `UserNotFound` | The given User could not be found. |
| NOT_FOUND | `UserProviderInfoNotFound` | The given UserProviderInfo could not be found. |
